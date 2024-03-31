package org.arouabendaya.theresidencehotel.service;

import lombok.RequiredArgsConstructor;
import org.arouabendaya.theresidencehotel.exception.InternalServerException;
import org.arouabendaya.theresidencehotel.exception.RessourceNotFoundException;
import org.arouabendaya.theresidencehotel.model.Room;
import org.arouabendaya.theresidencehotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);


        }


        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistincRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isEmpty()) {
            throw new RessourceNotFoundException("Sorry, Room not found!");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null
                ;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isPresent()) {
            roomRepository.deleteById(roomId);
        }

    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes)  {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RessourceNotFoundException("Room not found"));
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (photoBytes != null && photoBytes.length > 0){
            try {
                room.setPhoto(new SerialBlob(photoBytes));
            }catch (SQLException ex){
                throw new InternalServerException("Error updating photo");

            }
        }



        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }

}

