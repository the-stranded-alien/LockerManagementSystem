package lockermanagement.system.services;

import lockermanagement.system.models.Customer;
import lockermanagement.system.models.Locker;
import lockermanagement.system.repositories.LockerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class LockerService {

    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private CustomerService customerService;

    public Locker findLockerByPinCodeAndStatus(Integer pinCode, Locker.Status status) {
        Optional<Locker> optional = this.lockerRepository.findTopByLocationPinCodeAndStatus(pinCode, status);
        Locker locker = null;
        if(optional.isPresent()) {
            locker = optional.get();
        }
        return locker;
    }

    public Locker findLockerById(Long id) {
        Optional<Locker> optional = this.lockerRepository.findById(id);
        Locker locker = null;
        if(optional.isPresent()) {
            locker = optional.get();
        }
        return locker;
    }

    public Locker saveLocker(Locker locker) {
        return this.lockerRepository.save(locker);
    }

    public Locker addNewLocker(Integer pin) {
        if(pin < 100000 || pin > 999999) return null;
        Locker locker = new Locker();
        locker.createNewLocker(pin);
        this.saveLocker(locker);
        return locker;
    }

    public String book(Locker locker) {
        Customer customer = this.customerService.findCustomerByMobileNumber(locker.getCustomerPhoneNumber());
        if(customer == null) return "This Customer Doesn't Exist !";
        else {
            Locker availableLocker = this.findLockerByPinCodeAndStatus(locker.getLocationPinCode(), Locker.Status.AVAILABLE);
            if(availableLocker == null) return "No Locker Available at Given Location";
            else {
                Locker bookedLocker = availableLocker.bookLocker(locker.getCustomerPhoneNumber());
                this.saveLocker(bookedLocker);
                return "Success";
            }
        }
    }

    public boolean sendOTP(Locker locker) {
        if(locker.getStatus() == Locker.Status.BOOKED) {
            Random rand = new Random();
            Integer otp = rand.nextInt((9999 - 100) + 1) + 10;
            locker.sendOTP(otp);
            this.lockerRepository.save(locker);
            return true;
        } else return false;
    }

    public boolean open(Locker locker, Integer otp) {
        if(!Objects.equals(locker.getOtp(), otp)) return false;
        else {
            locker.open();
            this.lockerRepository.save(locker);
            return true;
        }
    }
}
