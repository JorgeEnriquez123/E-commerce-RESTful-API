package com.jorge.ecommerce.service;

import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.Role;
import com.jorge.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    protected Role findById(Long id) {
        log.debug("Finding role with id: {} using repository", id);
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    protected Role findByName(String name) {
        log.debug("Finding role by name: {} using repository", name);
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role with name: " + name + " not found"));
    }
}
