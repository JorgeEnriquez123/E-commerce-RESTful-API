package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.AddressLine;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.AddressLineRepository;
import com.jorge.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressLineService {
    private final AddressLineRepository addressLineRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public List<AddressLineDto> getAddressesByUserId(Long userId) {
        List<AddressLine> addressLines = addressLineRepository.findByUserId(userId)
                .orElse(Collections.emptyList());
        if(addressLines.isEmpty())
            throw new EntityNotFoundException("No address lines found for user with Id: " + userId);

        return addressLines.stream()
                .map(addressLine -> modelMapper.map(addressLine, AddressLineDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto saveAddressLineWithUserId(Long idUser, CreateAddressLineDto createAddressLineDto) {
        User existingUser = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("User with Id: " + idUser + " not found"));
        AddressLine newAddressLine = modelMapper.map(createAddressLineDto, AddressLine.class);
        newAddressLine.setUser(existingUser);
        addressLineRepository.save(newAddressLine);
        return modelMapper.map(newAddressLine, AddressLineDto.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto updateAddressLine(Long id, CreateAddressLineDto createAddressLineDto) {
        AddressLine addressLine = addressLineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No address line found for Id: " + id));
        modelMapper.map(createAddressLineDto, addressLine);
        addressLineRepository.save(addressLine);
        return modelMapper.map(addressLine, AddressLineDto.class);
    }
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddressLineOfUser(Long userId, Long addressLineId) {
        List<AddressLine> addressLines = addressLineRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("No address lines found for user with Id: " + userId));

        AddressLine newDefaultAddress = addressLines.stream()
                .filter(addressLine -> addressLine.getId().equals(addressLineId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No address line found for Id: " + addressLineId));

        if(!newDefaultAddress.getIsDefault()) {
            addressLines.stream()
                    .filter(AddressLine::getIsDefault)
                    .findFirst()
                    .ifPresent(addressLine -> {
                        addressLine.setIsDefault(false);
                        addressLineRepository.save(addressLine);
                    });
            newDefaultAddress.setIsDefault(true);
            addressLineRepository.save(newDefaultAddress);
        }
    }
}
