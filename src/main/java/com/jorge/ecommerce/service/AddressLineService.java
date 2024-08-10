package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.update.UpdateAddressLineDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.AddressLine;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.AddressLineRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AddressLineService {
    private final AddressLineRepository addressLineRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AddressLineService(AddressLineRepository addressLineRepository,
                              @Lazy UserService userService, ModelMapper modelMapper) {
        this.addressLineRepository = addressLineRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    protected AddressLine findById(Long id){
        log.debug("Finding address line by id: {} using repository", id);
        return addressLineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AddressLine with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    protected List<AddressLine> findByUserId(Long userId){
        log.debug("Finding address lines by user id: {} using repository", userId);
        List<AddressLine> addressLines = addressLineRepository.findByUserId(userId)
                .orElse(Collections.emptyList());
        if(addressLines.isEmpty()){
            throw new ResourceNotFoundException("AddressLines not found from User with id: " + userId);
        }
        return addressLines;
    }

    @Transactional(readOnly = true)
    protected AddressLine findByIdAndUserId(Long id, Long userId){
        log.debug("Finding address line by id: {} and user id: {} using repository", id, userId);
        return addressLineRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("AddressLine with id: " + id + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    protected AddressLine save(AddressLine addressLine){
        log.debug("Saving address line: {} using repository", addressLine);
        return addressLineRepository.save(addressLine);
    }

    @Transactional(readOnly = true)
    public List<AddressLineDto> getByUserId(Long userId) {
        List<AddressLine> addressLines = findByUserId(userId);
        return addressLines.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto saveAddressLine(Long userId, CreateAddressLineDto createAddressLineDto) {
        User user = userService.findById(userId);

        AddressLine newAddressLine = createAddressLineFromDto(createAddressLineDto);
        newAddressLine.setUser(user);
        AddressLine savedAddressLine = save(newAddressLine);

        return convertToDto(savedAddressLine);
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto updateAddressLineById(Long userId, Long addressLineId, UpdateAddressLineDto updateAddressLineDto) {
        AddressLine toUpdateAddressLine = findByIdAndUserId(addressLineId, userId);

        updateAddressLineFromDto(toUpdateAddressLine, updateAddressLineDto);

        AddressLine savedUpdatedAddressLine = save(toUpdateAddressLine);
        return convertToDto(savedUpdatedAddressLine);
    }

    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddressLine(Long userId, Long addressLineId) {
        List<AddressLine> addressLines = findByUserId(userId);

        AddressLine newDefaultAddress = addressLines.stream()
                .filter(addressLine -> addressLine.getId().equals(addressLineId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AddressLine with Id: " + addressLineId + " not found from User with id: " + userId));

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
        log.debug("Creating Address line from Dto: {}", createAddressLineDto);
        return modelMapper.map(createAddressLineDto, AddressLine.class);
    }

    private void updateAddressLineFromDto(AddressLine addressLine, UpdateAddressLineDto updateAddressLineDto) {
        log.debug("Updating Address Line from Dto: {}", updateAddressLineDto);
        modelMapper.map(updateAddressLineDto, addressLine);
        // Only Update Basic Info
    }

    private AddressLineDto convertToDto(AddressLine addressLine) {
        log.debug("Mapping address line: {} to Dto", addressLine);
        return modelMapper.map(addressLine, AddressLineDto.class);
    }
}
