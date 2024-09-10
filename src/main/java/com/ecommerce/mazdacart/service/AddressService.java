package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.payload.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface AddressService {
	/**
	 * Adds new address for the user. Throws exception for the same address for the user
	 *
	 * @param addressDTO
	 * @return
	 */
	AddressDTO createNewAddress (@Valid AddressDTO addressDTO);

	/**
	 * Gets all the Addresses int the system
	 *
	 * @return
	 */
	List<AddressDTO> getAllAddresses ();

	/**
	 * Gets all the addresses registered to the current user.
	 *
	 * @return
	 */
	List<AddressDTO> getAllAddressesForUser ();

	/**
	 * Updates the given address available to the user.
	 *
	 * @param addressDTO
	 * @param addressId
	 * @return
	 */
	AddressDTO updateUserAddress (@Valid AddressDTO addressDTO, Long addressId);

	/**
	 * Deletes any given address available to the user.
	 *
	 * @param addressId
	 * @return
	 */
	AddressDTO deleteAddress (@NotNull Long addressId);

	/**
	 * Delete any given address.
	 *
	 * @param addressId
	 * @return
	 */
	AddressDTO deleteAnyAddress (@NotNull Long addressId);
}
