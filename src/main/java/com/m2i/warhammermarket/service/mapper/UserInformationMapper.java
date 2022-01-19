package com.m2i.warhammermarket.service.mapper;

import org.springframework.stereotype.Service;

import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;

@Service
public class UserInformationMapper {
	
	public UserInformationDTO userInfoDAOToUserInfoDTO(UsersInformationDAO userInfoDao) {
        if (userInfoDao == null) {
            return null;
        } else {
        	UserInformationDTO userInfoDto = new UserInformationDTO();
        	userInfoDto.setId(userInfoDao.getId());
        	userInfoDto.setFirstName(userInfoDao.getFirstName());
        	userInfoDto.setLastName(userInfoDao.getLastName());
        	userInfoDto.setPhone(userInfoDao.getPhone());
        	userInfoDto.setBirthdate(userInfoDao.getBirthdate());
            return userInfoDto;
        }
    }
	
	public UsersInformationDAO userInfoDTOToUserInfoDAO(UserInformationDTO userInfoDto) {
        if (userInfoDto == null) {
            return null;
        } else {
        	UsersInformationDAO userInfoDao = new UsersInformationDAO();
        	userInfoDao.setId(userInfoDto.getId());
        	userInfoDao.setFirstName(userInfoDto.getFirstName());
        	userInfoDao.setLastName(userInfoDto.getLastName());
        	userInfoDao.setPhone(userInfoDto.getPhone());
        	userInfoDao.setBirthdate(userInfoDto.getBirthdate());
            return userInfoDao;
        }
    }

}
