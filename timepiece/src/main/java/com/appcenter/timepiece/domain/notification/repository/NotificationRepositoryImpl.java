package com.appcenter.timepiece.domain.notification.repository;


import com.appcenter.timepiece.domain.notification.dto.NotificationResponse;
import com.appcenter.timepiece.domain.notification.entity.Notification;
import com.appcenter.timepiece.domain.notification.entity.QNotification;
import com.appcenter.timepiece.domain.project.entity.QMemberProject;
import com.appcenter.timepiece.domain.project.entity.QProject;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QNotification notification = QNotification.notification;
    private QMemberProject receiver = new QMemberProject("receiver");
    private QMemberProject sender = new QMemberProject("sender");
    private QProject project = QProject.project;

    @Override
    public List<Notification> findAllByReceiverLargerThanNotificationId(Long receiverId, Long notificationId) {
        return queryFactory.selectFrom(notification)
                .where(notification.receiverId.eq(receiverId)
                        .and(notification.id.gt(notificationId)))
                .orderBy(notification.isChecked.asc(), notification.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Notification> findAllByReceiverLargerThanNotificationId(Long receiverId, Long projectId,
                                                                        Long notificationId) {
        return queryFactory.selectFrom(notification)
                .where(notification.receiverId.eq(receiverId)
                        .and(notification.projectId.eq(projectId))
                        .and(notification.id.gt(notificationId)))
                .orderBy(notification.isChecked.asc(), notification.createdAt.desc())
                .fetch();
    }

    @Override
    public List<NotificationResponse> finaAllByReceiverId(Long receiverId, LocalDateTime cursor, Boolean isChecked,
                                                          int size) {
        return queryFactory
                .select(Projections.constructor(NotificationResponse.class, notification, project, sender, receiver))
                .from(notification)
                .leftJoin(sender).on(notification.senderId.eq(sender.id))
                .leftJoin(receiver).on(notification.receiverId.eq(receiver.id))
                .leftJoin(project).on(notification.projectId.eq(project.id))
                .where(notification.receiverId.eq(receiverId)
                        .and(notification.isChecked.eq(isChecked))
                        .and(notification.isDeleted.isFalse())
                        .and(notification.createdAt.lt(cursor)))
                .orderBy(notification.isChecked.asc(), notification.createdAt.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public List<NotificationResponse> finaAllByReceiverIdInProject(Long receiverId, Long projectId,
                                                                   LocalDateTime cursor, Boolean isChecked, int size) {
        return queryFactory
                .select(Projections.constructor(NotificationResponse.class, notification, project, sender, receiver))
                .from(notification)
                .leftJoin(sender).on(notification.senderId.eq(sender.id))
                .leftJoin(receiver).on(notification.receiverId.eq(receiver.id))
                .leftJoin(project).on(notification.projectId.eq(project.id))
                .where(notification.receiverId.eq(receiverId)
                        .and(notification.projectId.eq(projectId))
                        .and(notification.isChecked.eq(isChecked))
                        .and(notification.isDeleted.isFalse())
                        .and(notification.createdAt.lt(cursor)))
                .orderBy(notification.isChecked.asc(), notification.createdAt.desc())
                .limit(size)
                .fetch();
    }
}
