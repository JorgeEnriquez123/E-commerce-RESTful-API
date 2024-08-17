package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.update.UpdateAddressLineDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.AddressLine;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.AddressLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressLineService {
    private final ModelMapper modelMapper;

    private final AddressLineRepository addressLineRepository;

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
    public List<AddressLineDto> getByUser(User user) {
        log.debug("Getting address lines from user with username: {}", user.getUsername());
        Long userId = user.getId();
        List<AddressLine> addressLines = findByUserId(userId);
        return addressLines.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto saveAddressLine(User user, CreateAddressLineDto createAddressLineDto) {
        log.debug("Adding address line: {}, for user with username: {}", createAddressLineDto, user.getUsername());
        AddressLine newAddressLine = createAddressLineFromDto(createAddressLineDto);
        newAddressLine.setUser(user);

        AddressLine savedAddressLine = save(newAddressLine);

        return convertToDto(savedAddressLine);
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto updateAddressLine(User user, Long addressLineId, UpdateAddressLineDto updateAddressLineDto) {
        log.debug("Updating address line: {}, from user with username: {}", updateAddressLineDto, user.getUsername());
        Long userId = user.getId();
        AddressLine toUpdateAddressLine = findByIdAndUserId(addressLineId, userId);

        updateAddressLineFromDto(toUpdateAddressLine, updateAddressLineDto);

        AddressLine savedUpdatedAddressLine = save(toUpdateAddressLine);
        return convertToDto(savedUpdatedAddressLine);
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto setDefaultAddressLine(User user, Long addressLineId) {
        log.debug("Setting default address line by id: {}, for user with username: {}", addressLineId, user.getUsername());
        Long userId = user.getId();

        AddressLine newDefaultaddressLine = findByIdAndUserId(addressLineId, userId);

        if(!newDefaultaddressLine.getIsDefault()){
            List<AddressLine> addressLinesFromUser = findByUserId(userId);
            addressLinesFromUser.stream()
                    .filter(AddressLine::getIsDefault)
                    .findFirst()
                    .ifPresent(addressLine -> {
                        addressLine.setIsDefault(false);
                        save(addressLine);
                    });

            newDefaultaddressLine.setIsDefault(true);

            return convertToDto(save(newDefaultaddressLine));
        }

        return convertToDto(newDefaultaddressLine);
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
