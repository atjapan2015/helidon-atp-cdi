package io.helidon.atp.mybatis.service.impl;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import io.helidon.atp.mybatis.entity.Manager;
import io.helidon.atp.mybatis.entity.ManagerExample;
import io.helidon.atp.mybatis.mapper.ManagerMapper;
import io.helidon.atp.mybatis.service.ManagerService;

@RequestScoped
public class ManagerServiceImpl implements ManagerService {

	@Inject
	ManagerMapper managerMapper;

	@Override
	public long countByExample(ManagerExample example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByExample(ManagerExample example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Manager record) {

		return managerMapper.insert(record);
	}

	@Override
	public int insertSelective(Manager record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Manager> selectByExample(ManagerExample example) {

		return managerMapper.selectByExample(example);
	}

	@Override
	public int updateByExampleSelective(Manager record, ManagerExample example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByExample(Manager record, ManagerExample example) {
		// TODO Auto-generated method stub
		return 0;
	}

}
