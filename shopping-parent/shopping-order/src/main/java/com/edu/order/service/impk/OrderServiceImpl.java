package com.edu.order.service.impk;

import com.edu.bean.TbOrderItem;
import com.edu.bean.TbOrderShipping;
import com.edu.mapper.TbOrderItemMapper;
import com.edu.mapper.TbOrderMapper;
import com.edu.mapper.TbOrderShippingMapper;
import com.edu.order.bean.Order;
import com.edu.order.dao.RedisDao;
import com.edu.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    @Value("${ORDER_KEY}")
    private String ORDER_KEY;
    @Value("${ORDER_INIT}")
    private String ORDER_INIT;
    @Value("${ORDER_ITEM_KEY}")
    private String ORDER_ITEM_KEY;
    @Autowired
    private RedisDao redisDao;


    @Override
    public String createOrder(Order order) {
        String s = redisDao.get(ORDER_KEY);
        if(StringUtils.isEmpty(s)) {
            redisDao.set(ORDER_KEY,ORDER_INIT) ;
        }
        // 保存订单信息，订单的id需要手动生成、可以使用uuid,但是呢？uuid太长
        long orderId = redisDao.incr(ORDER_KEY);
        // 补全数据
        order.setOrderId(orderId+"");
        order.setStatus(1);
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
        // 保存订单
        orderMapper.insertSelective(order);
        // 保存订单的项
        List<TbOrderItem> orderItems = order.getOrderItems();
        for(TbOrderItem orderItem:orderItems) {
            orderItem.setId(redisDao.incr(ORDER_ITEM_KEY)+"");
            orderItem.setOrderId(orderId+"");
            orderItemMapper.insertSelective(orderItem);
        }
        // 保存物流信息
        TbOrderShipping orderShipping = order.getOrderShipping();
        orderShipping.setOrderId(orderId+"");
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insertSelective(orderShipping);



        return orderId+"";
    }
}
