package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.handler.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.AddressLine;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.AddressLineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressLineService {
    private final AddressLineRepository addressLineRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    protected AddressLine findById(Long id){
        return addressLineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AddressLine with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    protected List<AddressLine> findByUserId(Long userId){
        List<AddressLine> addressLines = addressLineRepository.findByUserId(userId)
                .orElse(Collections.emptyList());
        if(addressLines.isEmpty()){
            throw new EntityNotFoundException("AddressLines not found from User with id: " + userId);
        }
        return addressLines;
    }

    @Transactional(readOnly = true)
    public List<AddressLineDto> getByUserId(Long userId) {
        List<AddressLine> addressLines = findByUserId(userId);
        return addressLines.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto saveAddressLineWithUserId(CreateAddressLineDto createAddressLineDto) {
        AddressLine newAddressLine = createAddressLineFromDto(createAddressLineDto);
        AddressLine savedAddressLine = addressLineRepository.save(newAddressLine);
        return convertToDto(savedAddressLine);
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto updateAddressLine(Long id, CreateAddressLineDto createAddressLineDto) {
        AddressLine toUpdateAddressLine = findById(id);
        updateAddressLineFromDto(toUpdateAddressLine, createAddressLineDto);
        AddressLine savedUpdatedAddressLine = addressLineRepository.save(toUpdateAddressLine);
        return convertToDto(savedUpdatedAddressLine);
    }
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddressLineOfUser(Long userId, Long addressLineId) {
        List<AddressLine> addressLines = findByUserId(userId);

        AddressLine newDefaultAddress = addressLines.stream()
                .filter(addressLine -> addressLine.getId().equals(addressLineId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("AddressLine with Id: " + addressLineId + " not found from User with id: " + userId));

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

    private AddressLine createAddressLineFromDto(CreateAddressLineDto createAddressLineDto) {
        User user = userService.findById(createAddressLineDto.getUserId());
        AddressLine newAddressLine = modelMapper.map(createAddressLineDto, AddressLine.class);
        newAddressLine.setUser(user);
        return newAddressLine;
    }

    private void updateAddressLineFromDto(AddressLine addressLine, CreateAddressLineDto createAddressLineDto) {
        modelMapper.map(createAddressLineDto, addressLine);
        // Not updating the User the AddressLine is linked to.
    }

    private AddressLineDto convertToDto(AddressLine addressLine) {
        return modelMapper.map(addressLine, AddressLineDto.class);
    }
}
