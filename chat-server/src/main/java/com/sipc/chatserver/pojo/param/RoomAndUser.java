package com.sipc.chatserver.pojo.param;

import com.sipc.chatserver.pojo.domain.Room;
import com.sipc.chatserver.pojo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomAndUser {
    private User user;
    private Room room;

    public RoomAndUser() {
        //此无参构造用于反序列化，不可删除
    }
}
