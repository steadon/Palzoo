package com.sipc.chatserver.pojo.param;

import com.sipc.chatserver.pojo.domain.Room;
import com.sipc.chatserver.pojo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomAndUser {
    User user;
    Room room;
}
