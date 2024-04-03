package org.arouabendaya.theresidencehotel.repository;

import org.arouabendaya.theresidencehotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

    List<BookedRoom> findByRoomId(Long roomId);

    BookedRoom findBybookingConfirmationCode(String confirmationCode);
}