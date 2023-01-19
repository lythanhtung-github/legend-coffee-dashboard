package com.cg.service.customerAvatar;

import com.cg.domain.entity.CustomerAvatar;
import com.cg.repository.CustomerAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class CustomerAvatarServiceImpl implements ICustomerAvatarService{

    @Autowired
    private CustomerAvatarRepository customerAvatarRepository;

    @Override
    public List<CustomerAvatar> findAll() {
        return null;
    }

    @Override
    public CustomerAvatar getById(Long id) {
        return customerAvatarRepository.getById(id);
    }

    @Override
    public Optional<CustomerAvatar> findById(Long id) {
        return customerAvatarRepository.findById(id);
    }

    @Override
    public CustomerAvatar save(CustomerAvatar customerAvatar) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
