
package com.masai.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.BidException;
import com.masai.exception.NotFoundException;
import com.masai.exception.TenderException;
import com.masai.exception.VendorException;
import com.masai.model.Bid;
import com.masai.model.Tender;
import com.masai.model.Vendor;
import com.masai.repository.BidRepository;
import com.masai.repository.TenderRepository;
import com.masai.repository.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {
	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private TenderRepository tenderRepository;

	@Autowired
	private BidRepository bidRepository;

	@Override
	public Vendor createVendor(Vendor vendor) throws VendorException {
		Optional<Vendor> existingVendor = vendorRepository.findByUsername(vendor.getUsername());
		if(existingVendor.isPresent()) {
			throw new VendorException("Vendor Already Registered with this UserName");
		}
		return vendorRepository.save(vendor);	
	}

	@Override
	public void updateVendorPassword(String username, String password, String newPassword) throws VendorException {
		Optional<Vendor> optionalVendor = vendorRepository.findByUsername(username);
		if (optionalVendor.isPresent()) {
			Vendor vendor = optionalVendor.get();
			if (vendor.getPassword().equals(password)) {
				vendor.setPassword(newPassword);
				vendorRepository.save(vendor);
			} else {
				throw new VendorException("Password is incorrect.");
			}
		} else {
			throw new VendorException("Vendor not found with username: " + username);
		}
	}

	@Override
	public List<Tender> viewAllTenders() throws TenderException {
		List<Tender> allTenders = tenderRepository.findAll();

		List<Tender> availableTenders = allTenders.stream().filter(tender -> (tender.getStatus().toString().equalsIgnoreCase("Available")))
				.collect(Collectors.toList());

		if (availableTenders.isEmpty()) {
			throw new TenderException("No Tenders available");
		} else {
			return availableTenders;
		}
	}

	@Override
	public Tender viewTendersById(Integer tenderId) throws TenderException {
		Optional<Tender> opt = tenderRepository.findById(tenderId);

		if (opt.isPresent()) {
			Tender viewTender = opt.get();
			if (viewTender.getStatus().toString().equalsIgnoreCase("available")) {
				return viewTender;
			} else {
				throw new TenderException("This Tender is not available for Bid");
			}
		} else {
			throw new TenderException("Tender not found");
		}
	}

	@Override
	public Bid placeBid(Integer tenderId, Integer vendorId, Bid bid) throws TenderException, VendorException, BidException {
		Optional<Tender> optionalTender = tenderRepository.findById(tenderId);
		if (optionalTender.isPresent()) {
			Tender tender = optionalTender.get();

		        if (!(tender.getStatus().toString().equalsIgnoreCase("available"))) {
		            throw new TenderException("Cannot place bid. Tender is not available with TenderId "+tenderId);
		        }

			Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
			if (optionalVendor.isPresent()) {
				Vendor vendor = optionalVendor.get();
				
			  List<Bid> bidList =  bidRepository.findExistingBidByTenderIdVendorId(vendorId, tenderId);
				if(bidList.size()>0) throw new BidException("You Can't place a Bid against a same Tender. Your Bid is already Exist.");
				bid.setTender(tender);
				bid.setVendor(vendor);
				tender.getBidList().add(bid);
				vendor.getBidList().add(bid);
				bidRepository.save(bid);
				tenderRepository.save(tender);
				vendorRepository.save(vendor);
			} else {
				throw new VendorException("Vendor not found with ID: " + vendorId);
			}
		} else {
			throw new TenderException("Tender not found with ID: " + tenderId);
		}
		return bid;
	}

	@Override
	public List<Bid> viewBidHistory(Integer vendorId) throws NotFoundException, VendorException {
		Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
		if (optionalVendor.isPresent()) {
			Vendor vendor = optionalVendor.get();
			List<Bid> bidList =  bidRepository.findBidHistoryByVendorId(vendorId);
			if(bidList.size()==0) {
				throw new NotFoundException("Bid Not Found");
			}
			return bidList;
		}else {
			throw new VendorException("Vendor Not Found with id "+vendorId);
		}
		
	}

}
