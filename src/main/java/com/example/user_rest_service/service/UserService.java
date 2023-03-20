package com.example.user_rest_service.service;

import com.example.user_rest_service.dto.UserDto;
import com.example.user_rest_service.dto.mapper.UserDbMapper;
import com.example.user_rest_service.dto.mapper.UserDtoMapper;
import com.example.user_rest_service.entity.User;
import com.example.user_rest_service.entity.UserDB;
import com.example.user_rest_service.exception.BusinessLogicException;
import com.example.user_rest_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDtoMapper userDtoMapper;

    @Autowired
    UserDbMapper userDbMapper;

    public UserDto createUser(UserDto userDto) throws BusinessLogicException {
        checkFieldsForCreate(userDto);
        LOG.info(String.format("Started mapped user from Dto with id=%d", userDto.getUserId()));
        User user = userDtoMapper.fromDto(userDto);
        LOG.info(String.format("Started mapped user to DB with id=%d", userDto.getUserId()));
        UserDB userForSave = userDbMapper.toDB(user);
        UserDB savedUser = null;
        try {
            savedUser = userRepository.save(userForSave);
            LOG.info(String.format("User with id=%s successfully saved to DB", savedUser.getUserId()));
        } catch (Exception e) {
            LOG.error(String.format("Can not save user with id=%d", userDto.getUserId()));
            e.printStackTrace();
        }
        User userFromDb = userDbMapper.fromDB(savedUser);
        UserDto savedUserDto = userDtoMapper.toDto(userFromDb);
        return savedUserDto;
    }

    public UserDto updateUser(UserDto userDto, Integer id) throws BusinessLogicException {
        checkFieldsForUpdate(userDto);
        UserDB userDB;
        try {
            LOG.info(String.format("Started find user for update with id=%d",id));
            userDB = userRepository.findById(String.valueOf(id)).get();
        } catch (Exception e) {
            LOG.error(String.format("Can not find user with id=%d", id));
            throw new BusinessLogicException(String.format("User with id=%d is not found", id));
        }
        User user = userDtoMapper.fromDto(userDto);
        userDB.setUserName(user.getUserName());
        userDB.setUserAge(user.getUserAge());
        UserDB updatedUser;
        try {
            updatedUser = userRepository.save(userDB);
            LOG.info(String.format("User with id=%s successfully updated in DB", updatedUser.getUserId()));
        } catch (Exception e) {
            LOG.error(String.format("Can not update user with id=%d", id));
            e.printStackTrace();
        }
        User userFromDb = userDbMapper.fromDB(userRepository.save(userDB));
        UserDto updatedUserDto = userDtoMapper.toDto(userFromDb);
        return updatedUserDto;

    }

    public List<UserDto> getUsers() {
        List<UserDB> userDBList = userRepository.findAll();
        LOG.info("Finished getting users from DB.Number of users =" + userDBList.size());
        List<User> userList = userDbMapper.fromDB(userDBList);
        List<UserDto> userDtoList = userDtoMapper.toDto(userList);
        return userDtoList;
    }

    public UserDto getUserById(Integer id) throws BusinessLogicException {
        UserDB userDB;
        try {
            userDB = userRepository.findById(String.valueOf(id)).get();
            LOG.info(String.format("Successfully find user with id=%d", id));
        } catch (Exception e) {
            LOG.error(String.format("User with id=%d is not found", id));
            throw new BusinessLogicException(String.format("User with id=%d is not found", id));
        }
        User user = userDbMapper.fromDB(userDB);
        UserDto userDto = userDtoMapper.toDto(user);
        return userDto;
    }

    public void deleteUsers() {
        LOG.info("Start delete all users");
        userRepository.deleteAll();
    }

    public void deleteUserById(Integer id) throws BusinessLogicException {
        try {
            userRepository.deleteById(String.valueOf(id));
            LOG.info(String.format("User with id=%d successfully deleted", id));
        } catch (Exception e) {
            LOG.error(String.format("Can not delete user with id=%d", id));
            throw new BusinessLogicException(String.format("User with id=%d is not found", id));
        }

    }

    private void checkFieldsForCreate(UserDto userDto) throws BusinessLogicException {
        if (userDto != null) {
            checkUserId(userDto.getUserId());
            checkUserName(userDto);
            checkUserAge(userDto);
        }
    }

    private void checkFieldsForUpdate(UserDto userDto) throws BusinessLogicException {
        if (userDto != null) {
            checkUserName(userDto);
            checkUserAge(userDto);

        }
    }

    private void checkUserId(Integer userId) throws BusinessLogicException {
        if (userId == 0) {
            LOG.error("User id field is missing");
            throw new BusinessLogicException("User id field is missing");
        } else {
            if (userId < 0) {
                LOG.error(String.format("Invalid value of User id=%d, must be greater than 0", userId));
                throw new BusinessLogicException(String.format("Invalid value of User id=%d, must be greater than 0", userId));
            }
            Optional<UserDB> userDB = userRepository.findById(String.valueOf(userId));
            if (userDB.isPresent() && userDB.get().getUserId().equals(String.valueOf(userId))) {
                LOG.error(String.format("User with id=%d already exist", userId));
                throw new BusinessLogicException(String.format("User with id=%d already exist", userId));
            }
        }

    }

    private void checkUserName(UserDto userDto) throws BusinessLogicException {
        if (!Optional.ofNullable(userDto.getUserName()).isPresent()) {
            LOG.error(String.format("User name field is missing for user with id=%d", userDto.getUserId()));
            throw new BusinessLogicException("User name field is missing");
        }
    }

    private void checkUserAge(UserDto userDto) throws BusinessLogicException {
        if (!Optional.ofNullable(userDto.getUserAge()).isPresent()) {
            LOG.error(String.format("User age field is missing for user with id=%d", userDto.getUserId()));
            throw new BusinessLogicException("User Age field is missing");
        } else {
            if (Integer.valueOf(userDto.getUserAge()) <= 0 || userDto.getUserAge().length() > 3) {
                LOG.error(String.format("Invalid value of User age=%s, must be greater than 0 and has no more than 3 letters", userDto.getUserAge()));
                throw new BusinessLogicException(String.format("Invalid value of User age=%s, must be greater than 0 and has no more than 3 letters", userDto.getUserAge()));
            }
        }

    }
}
