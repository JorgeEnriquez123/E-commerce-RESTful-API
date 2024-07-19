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
    private final UserService userService;
    private final ModelMapper modelMapper;

    protected AddressLine findAddressLineEntityById(Long id){
        return addressLineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AddressLine with id: " + id + " not found"));
    }
    public List<AddressLineDto> getAddressesByUserId(Long userId) {
        List<AddressLine> addressLines = addressLineRepository.findByUserId(userId)
                .orElse(Collections.emptyList());
        if(addressLines.isEmpty())
            throw new EntityNotFoundException("No address lines found for user with Id: " + userId);

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
        AddressLine toUpdateAddressLine = updateAddressLineFromDto(id, createAddressLineDto);
        AddressLine savedUpdatedAddressLine = addressLineRepository.save(toUpdateAddressLine);
        return convertToDto(savedUpdatedAddressLine);
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

    public AddressLine createAddressLineFromDto(CreateAddressLineDto createAddressLineDto) {
        User user = userService.findUserEntityById(createAddressLineDto.getUserId());
        AddressLine newAddressLine = modelMapper.map(createAddressLineDto, AddressLine.class);
        newAddressLine.setUser(user);
        return newAddressLine;
    }

    public AddressLine updateAddressLineFromDto(Long addressLineId, CreateAddressLineDto createAddressLineDto) {
        AddressLine toUpdateAddressLine = findAddressLineEntityById(addressLineId);
        modelMapper.map(createAddressLineDto, toUpdateAddressLine);

        // Not updating the User the AddressLine is linked to.
        // Might as well use an 'UpdateAddressLineDto'
        // User user = userService.findUserEntityById(createAddressLineDto.getUserId());
        // toUpdateAddressLine.setUser(user);

        return toUpdateAddressLine;
    }

    public AddressLineDto convertToDto(AddressLine addressLine) {
        return modelMapper.map(addressLine, AddressLineDto.class);
    }
}
