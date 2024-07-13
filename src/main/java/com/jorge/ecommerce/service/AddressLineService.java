package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.AddressLine;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.AddressLineRepository;
import com.jorge.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressLineService {
    private final AddressLineRepository addressLineRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public List<AddressLineDto> getAddressesByUserId(Long userId) {
        List<AddressLine> addressLines = addressLineRepository.findByUserId(userId).orElseThrow();
        List<AddressLineDto> addressLineDtos = new ArrayList<>();
        addressLines.forEach(
                addressLine -> {
                    AddressLineDto addressLineDto = modelMapper.map(addressLine, AddressLineDto.class);
                    addressLineDtos.add(addressLineDto);
                }
        );
        return addressLineDtos;
    }

    public AddressLineDto saveAddressLineWithUserId(Long idUser, AddressLineDto addressLineDto) {
        User existingUser = userRepository.findById(idUser).
                orElseThrow(() -> new EntityNotFoundException("User with Id: " + idUser + " not found"));
        AddressLine newAddressLine = modelMapper.map(addressLineDto, AddressLine.class);
        newAddressLine.setUser(existingUser);
        addressLineRepository.save(newAddressLine);
        return modelMapper.map(newAddressLine, AddressLineDto.class);
    }

    public AddressLineDto updateAddressLine(Long id, AddressLineDto addressLineDto) {
        AddressLine addressLine = addressLineRepository.findById(id).orElseThrow();
        addressLineDto.setId(id);
        modelMapper.map(addressLineDto, addressLine);
        addressLineRepository.save(addressLine);
        return modelMapper.map(addressLine, AddressLineDto.class);
    }
}
