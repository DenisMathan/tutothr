package tutothr.message.interfaces;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tutothr.common.MyBaseRepository;
import tutothr.message.Message;

import java.util.List;

@Repository
public interface MessageRepositoryI extends MyBaseRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
            "ORDER BY m.sentAt ASC, m.id ASC")
    List<Message> findConversation(@Param("userId1") Long userId1,
                                   @Param("userId2") Long userId2);

    List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);

    List<Message> findBySenderIdOrderBySentAtDesc(Long senderId);

    List<Message> findByReceiverIdAndReadFalseOrderBySentAtDesc(Long receiverId);

    long countByReceiverIdAndReadFalse(Long receiverId);

    List<Message> findByCourseIdOrderBySentAtDesc(Long courseId);
}