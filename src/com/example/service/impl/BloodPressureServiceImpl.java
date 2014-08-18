package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.BloodPressureDao;
import com.example.model.BloodPressure;
import com.example.service.BloodPressureService;

@Service("bloodPressureService")
public class BloodPressureServiceImpl implements BloodPressureService {

	@Autowired
	BloodPressureDao bloodPressureDao;

	@Override
	public void addBloodPressure(int userID, BloodPressure bloodPressure) {
		bloodPressureDao.addBloodPressure(userID, bloodPressure);
	}

}