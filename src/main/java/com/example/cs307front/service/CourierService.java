package com.example.cs307front.service;

import com.example.cs307front.interfaces.ICourier;
import com.example.cs307front.interfaces.ItemInfo;
import com.example.cs307front.interfaces.ItemState;
import com.example.cs307front.interfaces.LogInfo;
import com.example.cs307front.utils.MethodFactory;
import com.example.cs307front.utils.SqlFactory;
import com.example.cs307front.utils.annotations.Update;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author yjt
 * impl for courier
 */
public class CourierService implements ICourier {
    private final Predicate<LogInfo> identifyCheck =
            (id) -> id.type() == LogInfo.StaffType.Courier;


    private boolean checkItem(ItemInfo item, LogInfo log) {
        return
                item.retrieval().courier() == null &&
                        item.name() != null &&
                        item.$class() != null &&
                        item.$import().officer() == null &&
                        item.export().officer() == null &&
                        item.delivery().courier() == null &&
                        item.state() == null &&
                        Objects.equals(item.retrieval().city(), MethodFactory.getCity(log)) &&
                        !MethodFactory.checkItemExist(item.name());
    }

    @Override
    @Update
    public boolean newItem(LogInfo log, ItemInfo item) {
        if (identifyCheck.test(log)) {
            if (!checkItem(item, log)) {
                return false;
            }
            String sql = "insert into record(item_name, item_class, item_price, state, company_id) " +
                    "values (?,?,?,?,?)";
            return SqlFactory.handleUpdate(sql, item.name(), item.$class(), item.price(), 1, MethodFactory.getCompanyId(log));
        } else {
            return false;
        }
    }

    @Override
    @Update
    public boolean setItemState(LogInfo log, String name, ItemState s) {
        if (identifyCheck.test(log)) {
            if (!MethodFactory.checkItemExist(name)) {
                return false;
            }
            if (s == SqlFactory.mapState(MethodFactory.getItemState(name))) {
                return false;
            }
            Integer itemState = MethodFactory.getItemState(name);
            Integer nextState = SqlFactory.mapStateToInt(s);
            if (itemState == null){
                return false;
            }
            if (nextState == null){
                return false;
            }
            if(nextState >= 4 && nextState <= 8) {
                return false;
            }
            if(itemState >= 4 && itemState <= 8) {
                return false;
            }
            if (MethodFactory.checkCourier(log, name,6) && itemState >= 9){
                if (nextState == 9){
                    if (itemState != 9){
                        return false;
                    }
                }else if (nextState == 10){
                    if (itemState != 9){
                        return false;
                    }
                }else if (nextState == 11){
                    if (itemState != 10){
                        return false;
                    }
                }
            }else if (MethodFactory.checkCourier(log, name,1) && itemState <= 2){
                if (nextState > 3 || nextState - itemState != 1){
                    return false;
                }
            }else {
                return false;
            }
            String sql = """
                    update record set state = ? where item_name = ?
                    """;
            return SqlFactory.handleUpdate(sql,nextState,name);
        } else {
            return false;
        }
    }
}
