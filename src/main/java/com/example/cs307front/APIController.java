package com.example.cs307front;

import com.alibaba.fastjson2.JSON;
import main.interfaces.ItemInfo;
import main.interfaces.LogInfo;
import org.springframework.web.bind.annotation.*;
import main.service.DatabaseManipulation;
import main.utils.SqlFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/db")
public class APIController {

    DatabaseManipulation databaseManipulation = new DatabaseManipulation();

    static Map<String, LogInfo> users = new HashMap<>(10);
    static DatabaseManipulation importReady;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LogInfo staff) {
        String sql = "select type from staff where name = ? and password = ?";
        try {
            LogInfo me = SqlFactory.handleSingleResult(
                    SqlFactory.handleQuery(sql, staff.name(), staff.password()),
                    r -> {
                        Integer type = r.getInt(1);
                        String name = staff.name();
                        String password = staff.password();
                        if (type == null) {
                            return null;
                        } else if (type == 1) {
                            return new LogInfo(name,
                                    LogInfo.StaffType.SustcManager,
                                    password
                            );
                        } else if (type == 2) {
                            return new LogInfo(name,
                                    LogInfo.StaffType.CompanyManager,
                                    password
                            );
                        } else if (type == 3) {
                            return new LogInfo(name,
                                    LogInfo.StaffType.SeaportOfficer,
                                    password
                            );
                        } else if (type == 4) {
                            return new LogInfo(name,
                                    LogInfo.StaffType.Courier,
                                    password
                            );
                        } else {
                            return null;
                        }
                    }
            );
            String token;
            if (me != null) {
                token = UUID.randomUUID().toString();
                users.put(token, me);
                return Result.ok(me, token);
            } else {
                return Result.error("wrong name or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/me")
    public Result<?> tokenCheck(@RequestParam("token") String token) {
        System.out.println(token);
        if (!users.containsKey(token)) {
            return Result.error("no login!");
        } else {
            LogInfo me = users.get(token);
            return Result.ok(me);
        }
    }

    @GetMapping("/api1")
    public Result<?> api1(@RequestParam("database") String database, @RequestParam("root") String root,
                          @RequestParam("pass") String pass) {
        return Result.ok("connect successful");
    }

    @GetMapping("/api2")
    public Result<?> api2(@RequestParam("recordsCSV") String recordsCSV,
                          @RequestParam("staffsCSV") String staffsCSV) {
        if (importReady == null){
            return Result.error("Constructor invoke first!");
        }
        importReady.$import(recordsCSV,staffsCSV);
        return Result.ok("---");
    }

    @GetMapping("/api3")
    public Result<?> api3(@RequestParam("logInfo") String log) {
        return Result.ok(databaseManipulation.getCompanyCount(JSON.parseObject(log, LogInfo.class)));
    }

    @GetMapping("/api4")
    public Result<?> api4(@RequestParam("logInfo") String log) {
        return Result.ok(databaseManipulation.getCityCount(JSON.parseObject(log, LogInfo.class)));
    }

    @GetMapping("/api5")
    public Result<?> api5(@RequestParam("logInfo") String log) {
        return Result.ok(databaseManipulation.getCourierCount(JSON.parseObject(log, LogInfo.class)));
    }

    @GetMapping("/api6")
    public Result<?> api6(@RequestParam("logInfo") String log) {
        return Result.ok(databaseManipulation.getShipCount(JSON.parseObject(log, LogInfo.class)));
    }

    @GetMapping("/api7")
    public Result<?> api7(@RequestParam("logInfo") String log, @RequestParam("name") String name) {
        return Result.ok(databaseManipulation.getItemInfo(JSON.parseObject(log, LogInfo.class), name));
    }

    @GetMapping("/api8")
    public Result<?> api8(@RequestParam("logInfo") String log, @RequestParam("name") String name) {
        return Result.ok(databaseManipulation.getShipInfo(JSON.parseObject(log, LogInfo.class), name));
    }

    @GetMapping("/api9")
    public Result<?> api9(@RequestParam("logInfo") String log, @RequestParam("code") String code) {
        return Result.ok(databaseManipulation.getContainerInfo(JSON.parseObject(log, LogInfo.class), code));
    }

    @GetMapping("/api10")
    public Result<?> api10(@RequestParam("logInfo") String log, @RequestParam("name") String name) {
        return Result.ok(databaseManipulation.getStaffInfo(JSON.parseObject(log, LogInfo.class), name));
    }



    @PostMapping("/api11")
    public Result<?> api11(@RequestBody Api11 api) {
        return Result.ok(databaseManipulation.newItem(api.getLogInfo(), api.getItem()));
    }

    @GetMapping("/api12")
    public Result<?> api12(@RequestParam("logInfo") String log, @RequestParam("name") String name, @RequestParam("s") Integer s) {
        return Result.ok(databaseManipulation.setItemState(JSON.parseObject(log, LogInfo.class), name, SqlFactory.mapState(s)));
    }

    @GetMapping("/api13")
    public Result<Double> api13(@RequestParam("logInfo") String log,
                                @RequestParam("city") String city,
                                @RequestParam("itemClass") String itemClass) {
        return Result.ok(databaseManipulation.getExportTaxRate(JSON.parseObject(log, LogInfo.class), city, itemClass));
    }

    @GetMapping("/api14")
    public Result<Double> api14(@RequestParam("logInfo") String log,
                                @RequestParam("city") String city,
                                @RequestParam("itemClass") String itemClass) {
        return Result.ok(databaseManipulation.getImportTaxRate(JSON.parseObject(log, LogInfo.class), city, itemClass));
    }

    @GetMapping("/api15")
    public Result<Boolean> api15(@RequestParam("logInfo") String log,
                                 @RequestParam("itemName") String itemName,
                                 @RequestParam("containerCode") String containerCode) {
        return Result.ok(databaseManipulation.loadItemToContainer(JSON.parseObject(log, LogInfo.class), itemName, containerCode));
    }

    @GetMapping("/api16")
    public Result<Boolean> api16(@RequestParam("logInfo") String log,
                                 @RequestParam("shipName") String shipName,
                                 @RequestParam("containerCode") String containerCode) {
        return Result.ok(databaseManipulation.loadContainerToShip(JSON.parseObject(log, LogInfo.class), shipName, containerCode));
    }

    @GetMapping("/api17")
    public Result<Boolean> api17(@RequestParam("logInfo") String log,
                                 @RequestParam("shipName") String shipName) {
        return Result.ok(databaseManipulation.shipStartSailing(JSON.parseObject(log, LogInfo.class), shipName));
    }

    @GetMapping("/api18")
    public Result<Boolean> api18(@RequestParam("logInfo") String log,
                                 @RequestParam("item") String item) {
        return Result.ok(databaseManipulation.unloadItem(JSON.parseObject(log, LogInfo.class), item));
    }

    @GetMapping("/api19")
    public Result<Boolean> api19(@RequestParam("logInfo") String log,
                                 @RequestParam("item") String item) {
        return Result.ok(databaseManipulation.itemWaitForChecking(JSON.parseObject(log, LogInfo.class), item));
    }

    @GetMapping("/api20")
    public Result<?> api20(@RequestParam("logInfo") String log) {
        return Result.ok(databaseManipulation.getAllItemsAtPort(JSON.parseObject(log, LogInfo.class)));
    }

    @GetMapping("/api21")
    public Result<?> api21(@RequestParam("logInfo") String log, @RequestParam("itemName") String itemName, @RequestParam("success") Boolean success) {
        return Result.ok(databaseManipulation.setItemCheckState(JSON.parseObject(log, LogInfo.class), itemName, success));
    }
}

class Api11{
    private LogInfo logInfo;
    private ItemInfo item;

    public LogInfo getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(LogInfo logInfo) {
        this.logInfo = logInfo;
    }

    public ItemInfo getItem() {
        return item;
    }

    public void setItem(ItemInfo item) {
        this.item = new ItemInfo(
                item.name(),
                item.$class(),
                item.price(),
                null,
                new ItemInfo.RetrievalDeliveryInfo(item.retrieval().city(),null),
                new ItemInfo.RetrievalDeliveryInfo(item.delivery().city(), null),
                new ItemInfo.ImportExportInfo(item.$import().city(),null,item.$import().tax()),
                new ItemInfo.ImportExportInfo(item.export().city(),null,item.export().tax())
        );
    }

}