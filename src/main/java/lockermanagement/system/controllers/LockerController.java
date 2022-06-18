package lockermanagement.system.controllers;

import lockermanagement.system.models.Locker;
import lockermanagement.system.services.CustomerService;
import lockermanagement.system.services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lockers")
public class LockerController {
    @Autowired
    private LockerService lockerService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<String> addLocker(@RequestBody Locker locker) {
        Locker newLocker = this.lockerService.addNewLocker(locker.getLocationPinCode());
        if(newLocker != null) {
            return new ResponseEntity<>("Locker Created With Id : " + newLocker.getId().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to Create Locker With Given Pin Code !", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Locker> status(@PathVariable("id") Long id) {
        Locker locker = this.lockerService.findLockerById(id);
        return new ResponseEntity<>(locker, HttpStatus.OK);
    }

    @PostMapping("/book")
    public ResponseEntity<String> book(@RequestBody Locker locker) {
        String bookingStatus = this.lockerService.book(locker);
        if(bookingStatus == "Success") {
            return new ResponseEntity<>("Locker Booked Successfully !", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(bookingStatus, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/notify")
    public ResponseEntity<String> sendOTP(@PathVariable(value = "id") Long id) {
        Locker locker = this.lockerService.findLockerById(id);
        if(locker == null) {
            return new ResponseEntity<>("Locker Not Found !", HttpStatus.BAD_REQUEST);
        }
        boolean isOtpSent = this.lockerService.sendOTP(locker);
        if(isOtpSent) {
            return new ResponseEntity<>("OTP Send Successfully, OTP = " + locker.getOtp().toString(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Can't Send OTP For This Locker !", HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/open")
    public ResponseEntity<String> open(@RequestBody Locker locker, @PathVariable("id") Long id) {
        Locker existingLocker = this.lockerService.findLockerById(id);
        if(existingLocker == null) {
            return new ResponseEntity<>("Locker Not Found !", HttpStatus.BAD_REQUEST);
        }
        else if(existingLocker.getStatus() != Locker.Status.OTP_SENT) {
            return new ResponseEntity<>("Locker Not Accessible !", HttpStatus.BAD_REQUEST);
        }
        else {
            boolean isLockerOpened = this.lockerService.open(existingLocker, locker.getOtp());
            if(isLockerOpened) return new ResponseEntity<>("Locker Opened !", HttpStatus.OK);
            else return new ResponseEntity<>("Invalid OTP !", HttpStatus.BAD_REQUEST);
        }
    }
}
