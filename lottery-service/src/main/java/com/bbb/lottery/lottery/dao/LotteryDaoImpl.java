package com.bbb.lottery.lottery.dao;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.bbb.lottery.lottery.LotteryMessage;
import com.bbb.lottery.lottery.exceptions.LotteryUnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

@Repository
@Slf4j
@XRayEnabled
public class LotteryDaoImpl implements LotteryDao {
    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "bbb_lottery";

    @Autowired
    public LotteryDaoImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void saveLottery(String lotteryId, String lotteryNumber) {

        HashMap<String, AttributeValue> attributes = new HashMap<String,AttributeValue>();
        attributes.put("id", AttributeValue.builder().s(UUID.randomUUID().toString()).build());
        attributes.put("date", AttributeValue.builder().
                            s(LocalDate.now().getMonth().getValue() + "/" + LocalDate.now().getYear())
                            .build());
        attributes.put("lottery_id", AttributeValue.builder().s(lotteryId).build());
        attributes.put("lottery_number", AttributeValue.builder().s(lotteryNumber).build());

        PutItemRequest request = PutItemRequest.builder()
                                                .tableName(TABLE_NAME)
                                                .item(attributes)
                                                .build();
        try {
            dynamoDbClient.putItem(request);
            log.debug("Lottery details was successfully saved");
        } catch (ResourceNotFoundException ex) {
            log.error("Table {} not found. Error:" +ex.getMessage(), ex);
            throw new LotteryUnknownException(LotteryMessage.UNKNOWN_ERROR.getMessage());
        } catch (DynamoDbException ex) {
            log.error("Error while saving lottery details Error:" +ex.getMessage(), ex);
            throw new LotteryUnknownException(LotteryMessage.UNKNOWN_ERROR.getMessage());
        }
    }

    @PreDestroy
    private void cleanup() {
        dynamoDbClient.close();
    }
}
