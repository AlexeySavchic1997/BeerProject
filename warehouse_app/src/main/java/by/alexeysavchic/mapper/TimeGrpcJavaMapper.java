package by.alexeysavchic.mapper;

import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper
public interface TimeGrpcJavaMapper {
    default LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds()), ZoneOffset.UTC);
    }
}
