package com.masai.service;

import java.util.List;

import com.masai.exception.BidException;
import com.masai.exception.NotFoundException;
import com.masai.exception.TenderException;
import com.masai.exception.VendorException;
import com.masai.model.Bid;
import com.masai.model.Tender;
import com.masai.model.Vendor;

public interface VendorService {

	public Vendor createVendor(Vendor vendor) throws VendorException;


	public void updateVendorPassword(String username, String password, String newPassword) throws VendorException;


	public List<Tender> viewAllTenders() throws TenderException;


	public Tender viewTendersById(Integer tenderId) throws TenderException;

	public Bid placeBid(Integer tenderId, Integer vendorId, Bid bid) throws TenderException, VendorException, BidException;
	public List<Bid> viewBidHistory(Integer vendorId) throws NotFoundException, VendorException;

}
