package com.bilgeadam.service;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.IAuthMapper;
import com.bilgeadam.rabbitmq.producer.RegisterMailProducer;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.IAuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.entity.enums.ERoles;
import com.bilgeadam.repository.entity.enums.EStatus;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository iAuthRepository;
    private final IUserManager iUserManager;
    private final JwtTokenManager jwtTokenManager;
    private final RegisterProducer registerProducer;
    private final RegisterMailProducer mailProducer;

    public AuthService(IAuthRepository iAuthRepository, IUserManager iUserManager, JwtTokenManager jwtTokenManager, RegisterProducer registerProducer, RegisterMailProducer mailProducer) {
        super(iAuthRepository);
        this.iAuthRepository = iAuthRepository;
        this.iUserManager = iUserManager;
        this.jwtTokenManager = jwtTokenManager;
        this.registerProducer = registerProducer;
        this.mailProducer = mailProducer;
    }
    @Transactional // bir hata olduğu zaman burada yaptığı bütün işlemleri geri alır.
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        save(auth);
        try {
            iUserManager.createUser(IAuthMapper.INSTANCE.toNewCreateUserRequestDto(auth));
        } catch (Exception e) {
      //      delete(auth);
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }

        RegisterResponseDto registerResponseDto = IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        return registerResponseDto;
    }

    @Transactional // bir hata olduğu zaman burada yaptığı bütün işlemleri geri alır.
    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());

        try {
            save(auth);
            /**
             * Rabbit mq ile haberleşme sağlayacağız.
             */
            registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));
            mailProducer.sendActivationCode(IAuthMapper.INSTANCE.toRegisterMailModel(auth));
        } catch (Exception e) {
            //      delete(auth);
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }

        RegisterResponseDto registerResponseDto = IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        return registerResponseDto;
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> auth = iAuthRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        if (!auth.get().getStatus().equals(EStatus.ACTIVE))
            throw new AuthManagerException(ErrorType.NOT_ACTIVE_ACCOUNT);


        return jwtTokenManager.createToken(auth.get().getId(), auth.get().getRole()).orElseThrow(() -> {
            throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
        });

    }

    public Boolean activationCheck(ActivitaionRequestDto dto) {
        Optional<Auth> auth = findById(dto.getId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (dto.getActivationCode().equals(auth.get().getActivationCode())) {
            auth.get().setStatus(EStatus.ACTIVE);
            update(auth.get());
            iUserManager.activateStatus(auth.get().getId());
            return true;
        } else throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
    }

    public Boolean update(UpdateEmailOrUsernameRequestDto dto) {
        Optional<Auth> auth = findById(dto.getAuthId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setEmail(dto.getEmail());
        auth.get().setUsername(dto.getUsername());
        update(auth.get());
        return true;
    }

    public Boolean updateEmailOrUsername(UpdateEmailOrUsernameRequestDto dto) {
        Optional<Auth> auth = findById(dto.getAuthId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setEmail(dto.getEmail());
        auth.get().setUsername(dto.getUsername());
        update(auth.get());
        return true;
    }
    @Transactional
    public Boolean delete(Long id) {
        Optional<Auth> auth = findById(id);
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        iUserManager.delete(id);
        return true;
    }

    public List<Long> findByRole(String role) {

        List<Auth> list = iAuthRepository.findByRole(ERoles.valueOf(role));
        List<Long> idList = new ArrayList<>();
        list.forEach(x-> idList.add(x.getId()));
        return idList;
    }
}
