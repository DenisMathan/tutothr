package tutothr.message.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = """
        SELECT * FROM message m WHERE m.id IN (
            SELECT MAX(m2.id) FROM message m2
            WHERE m2.sender_id = :userId OR m2.receiver_id = :userId
            GROUP BY CASE 
                WHEN m2.sender_id = :userId THEN m2.receiver_id 
                ELSE m2.sender_id 
            END
        ) ORDER BY m.sent_at DESC
        """,
            countQuery = """
        SELECT COUNT(DISTINCT CASE 
            WHEN m.sender_id = :userId THEN m.receiver_id 
            ELSE m.sender_id 
        END) 
        FROM message m 
        WHERE m.sender_id = :userId OR m.receiver_id = :userId
        """,
            nativeQuery = true)
    Page<Message> findLatestConversations(@Param("userId") Long userId, Pageable pageable);
}