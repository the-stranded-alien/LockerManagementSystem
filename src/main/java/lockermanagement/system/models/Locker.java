package lockermanagement.system.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Status {
        AVAILABLE,
        BOOKED,
        OTP_SENT;
    };
    private Integer locationPinCode;
    private Integer otp;
    private Long customerPhoneNumber;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Locker bookLocker(Long phoneNumber) {
        this.customerPhoneNumber = phoneNumber;
        this.status = Status.BOOKED;
        return this;
    }

    public Locker createNewLocker(Integer pin) {
        this.status = Status.AVAILABLE;
        this.locationPinCode = pin;
        return this;
    }

    public Locker sendOTP(Integer otp) {
        this.status = Status.OTP_SENT;
        this.otp = otp;
        return this;
    }

    public void open() {
        this.status = Status.AVAILABLE;
        this.customerPhoneNumber = null;
        this.otp = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Locker locker = (Locker) o;
        return id != null && Objects.equals(id, locker.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
