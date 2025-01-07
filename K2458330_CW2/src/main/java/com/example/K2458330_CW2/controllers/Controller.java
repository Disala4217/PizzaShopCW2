package com.example.K2458330_CW2.controllers;

import com.example.K2458330_CW2.DataBaseConnection;
import com.example.K2458330_CW2.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {
    private static K2458330UserProfile userProfile;
    private final K2458330Order order = new K2458330Order();
    private final K2458330FeedbackManager feedbackManager = new K2458330FeedbackManager();
    public int pizzaPrice;
    public double Predefinedprice;
    Scanner scanner = new Scanner(System.in);
    K2458330PizzaBuilder builder = new K2458330PizzaBuilder();

    public static String LoginUser(String UserName, String Password) {
        String id = "";
        String sql = "SELECT user_id, username, address, loyalty_points FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, UserName);
            preparedStatement.setString(2, Password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Build userProfile object
                userProfile = new K2458330UserProfileBuilder()
                        .setUserName(resultSet.getString("username"))
                        .setAddress(resultSet.getString("address"))
                        .setLoyaltyPoints(resultSet.getInt("loyalty_points"))
                        .build();

                System.out.println("User logged in: " + userProfile.getUserName());
                System.out.println("User loyalty points: " + userProfile.getLoyaltyPoints());

                // Get the user_id
                id = Integer.toString(resultSet.getInt("user_id"));
            } else {
                // Invalid username or password
                id = "Invalid username or password.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while retrieving the user record.");
            id = "Error occurred while accessing the database.";
        }

        return id;

    }

    public static void RegisterUser(String username, String password, String address) {
        String insertQuery = "INSERT INTO users (username,password,address) VALUES (?, ?, ?)";
        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, address);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("user registered successfully!");
            } else {
                System.out.println("Failed to register the user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseConnection.disconnect();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> handleRegister(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String address = data.get("address");

        System.out.println("Received Username: " + username);
        System.out.println("Received Password: " + password);
        System.out.println("Received Address: " + address);
        RegisterUser(username, password, address);

        Map<String, String> response = Map.of("message", "Registered Successfully!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> handleLogin(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String ID = LoginUser(username, password);
        Map<String, String> response = Map.of("ID", ID, "username", userProfile.getUserName(), "address", userProfile.getAddress(), "FavPizza", userProfile.getFavorite(), "Loyelty", Integer.toString(userProfile.getLoyaltyPoints()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ratePizza")
    public ResponseEntity<Map<String, String>> ratePizza(@RequestBody Map<String, Object> data) {
        try {
            int rating = Integer.parseInt(data.get("ratings").toString());
            int pizzaId = Integer.parseInt(data.get("pizzaID").toString());

            K2458330FeedbackPizza feedbackPizzaCommand = new K2458330FeedbackPizza(rating, pizzaId);
            feedbackManager.executeCommand(feedbackPizzaCommand);

            Map<String, String> response = Map.of("msg", "ok");
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Map<String, String> response = Map.of("msg", "invalid number format");
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = Map.of("msg", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/rateOrder")
    public ResponseEntity<Map<String, String>> rateOrder(@RequestBody Map<String, Object> data) {
        try {
            int rating = Integer.parseInt(data.get("ratings").toString());
            int orderId = Integer.parseInt(data.get("orderId").toString());

            // Use the instance of FeedbackManager to execute the command
            K2458330FeedbackOrder feedbackOrderCommand = new K2458330FeedbackOrder(rating, orderId);
            feedbackManager.executeCommand(feedbackOrderCommand);

            Map<String, String> response = Map.of("msg", "ok");
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Map<String, String> response = Map.of("msg", "invalid number format");
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = Map.of("msg", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/pizzaBuilder")
    public ResponseEntity<Map<String, Object>> createPizza(@RequestBody Map<String, Object> data) {
        if (data.get("crust") == null || data.get("sauce") == null || data.get("cheese") == null) {
            String sql = "SELECT `name`, `price`, `crust`, `sauce`, `cheese`, `toppings` FROM `pizzas` WHERE name=?";
            try (Connection connection = DataBaseConnection.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, (String) data.get("name"));
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String crustType = resultSet.getString("crust");
                    String sauceType = resultSet.getString("sauce");
                    String cheeseType = resultSet.getString("cheese");
                    double price = resultSet.getDouble("price");
                    Predefinedprice += price;

                    List<String> toppingsList;
                    String toppings = resultSet.getString("toppings");
                    if (toppings != null && !toppings.isEmpty()) {
                        toppingsList = Arrays.asList(toppings.split(","));
                    } else {
                        toppingsList = new ArrayList<>();
                    }

                    String message = makePizza(builder, (String) data.get("name"), crustType, sauceType, cheeseType, toppingsList);

                    Map<String, Object> response = Map.of("name", data.get("name"), "price", price, "message", message);
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, Object> errorResponse = Map.of("message", "Pizza not found in the database");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Map<String, Object> errorResponse = Map.of(
                        "message", "Error fetching pizza details from the database",
                        "error", e.getMessage()
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } else {
            String name = (String) data.get("name");
            String crustType = (String) data.get("crust");
            String sauceType = (String) data.get("sauce");
            String cheeseType = (String) data.get("cheese");
            List<String> toppingsList = (List<String>) data.get("toppings");
            // Use the builder to construct the pizza
            String x = makePizza(builder, name, crustType, sauceType, cheeseType, toppingsList);
            order.addPizza(builder.build());
            // Create a response map
            Map<String, Object> response = Map.of("name", name, "price", pizzaPrice, "message", x);

            // Return a JSON response
            return ResponseEntity.ok(response);
        }

    }

    @PostMapping("/FavPizza")
    public ResponseEntity<Map<String, String>> FavPizza(@RequestBody Map<String, Object> data) {
        String name = (String) data.get("name");
        String crustType = (String) data.get("crust");
        String sauceType = (String) data.get("sauce");
        String cheeseType = (String) data.get("cheese");
        List<String> toppingsList = (List<String>) data.get("toppings");
        if (userProfile != null) {
            String x = makeFavPizza(builder, name, crustType, sauceType, cheeseType, toppingsList);

            // Create a response map
            Map<String, String> response = Map.of("message", x);

            // Return a JSON response
            return ResponseEntity.ok(response);

        } else {
            Map<String, String> response = Map.of("message", "Login First");

            // Return a JSON response
            return ResponseEntity.ok(response);

        }

    }

    @GetMapping("/predefined")
    public List<Map<String, Object>> getPredefinedPizzas() {
        List<Map<String, Object>> pizzas = new ArrayList<>();
        String sql = "SELECT p.pizza_id, p.name, p.price, p.rating FROM pizzas p LEFT JOIN user_favorite_pizzas uf ON p.pizza_id = uf.pizza_id WHERE p.pizza_type = 'predefined' OR uf.user_id = (SELECT user_id FROM users WHERE username = ?)";
        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userProfile.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> pizza = new HashMap<>();
                pizza.put("name", resultSet.getString("name"));
                pizza.put("price", resultSet.getDouble("price"));
                pizzas.add(pizza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while fetching predefined pizzas.");
        }
        return pizzas;

    }

    @GetMapping("/payment-details")
    public ResponseEntity<List<Map<String, Object>>> getPaymentDetails() {
        userProfile.updateLoyeltyPoints();

        String sql = "SELECT u.username, u.address, u.loyalty_points, o.total_price, o.order_date " +
                "FROM users u INNER JOIN orders o ON u.user_id = o.user_id WHERE o.order_id = ?;";

        List<Map<String, Object>> paymentDetailsList = new ArrayList<>();

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, order.getOrderID());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> paymentDetails = new HashMap<>();

                // Map fields
                paymentDetails.put("username", resultSet.getString("username"));
                paymentDetails.put("address", resultSet.getString("address"));
                paymentDetails.put("orderDate", resultSet.getDate("order_date").toString());
                int loyaltyPoints = resultSet.getInt("loyalty_points");
                double totalPrice = resultSet.getDouble("total_price");

                paymentDetails.put("loyaltyPoints", loyaltyPoints);

                // Calculate Loyalty Discount
                int loyaltyDiscount = 0;
                if (loyaltyPoints > 10) {
                    loyaltyDiscount = 10; // 10% discount for >10 points
                } else if (loyaltyPoints > 5) {
                    loyaltyDiscount = 5; // 5% discount for >5 points
                }

                // Calculate Christmas or New Year Discounts
                int christmasDiscount = 0;
                int newYearDiscount = 0;
                LocalDate currentDate = LocalDate.now();
                Month currentMonth = currentDate.getMonth();

                if (currentMonth == Month.DECEMBER) {
                    christmasDiscount = 10; // 10% discount during December
                } else if (currentMonth == Month.APRIL) {
                    newYearDiscount = 20; // 20% discount during April
                }

                // Calculate final discount percentage by combining valid discounts
                double totalDiscountRate = 0.0;
                if (loyaltyDiscount > 0) {
                    totalDiscountRate += loyaltyDiscount;
                }
                if (christmasDiscount > 0) {
                    totalDiscountRate += christmasDiscount;
                }
                if (newYearDiscount > 0) {
                    totalDiscountRate += newYearDiscount;
                }

                // Apply discounts on the total price
                double discountAmount = totalPrice * (totalDiscountRate / 100);
                double finalPrice = totalPrice - discountAmount;

                // Map the calculated values to the response
                paymentDetails.put("totalPrice", totalPrice);
                paymentDetails.put("loyaltyDiscount", loyaltyDiscount);
                paymentDetails.put("christmasDiscount", christmasDiscount);
                paymentDetails.put("newYearDiscount", newYearDiscount);
                paymentDetails.put("finalPrice", finalPrice);

                paymentDetailsList.add(paymentDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Map.of("error", "Failed to fetch payment details.")));
        }

        if (paymentDetailsList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonList(Map.of("error", "No payment details found for the given order ID.")));
        }

        return ResponseEntity.ok(paymentDetailsList);
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<List<Map<String, Object>>> getUserDetails() {

        String sql = "SELECT \n" +
                "    u.user_id,\n" +
                "    u.username AS user_name,\n" +
                "    u.loyalty_points,\n" +
                "    u.address,\n" +
                "    COUNT(DISTINCT o.order_id) AS total_orders,\n" +
                "    COUNT(od.pizzaName) AS total_pizzas_ordered,\n" +
                "    GROUP_CONCAT(DISTINCT CONCAT(\n" +
                "        p.name, ' (Crust: ', p.crust, ', Sauce: ', p.sauce, ', Cheese: ', p.cheese, \n" +
                "        ', Toppings: ', p.toppings, ', Price: ', p.price, ', Rating: ', p.rating, \n" +
                "        ', Type: ', p.pizza_type, ')'\n" +
                "    ) SEPARATOR '; ') AS favorite_pizzas_details\n" +
                "FROM \n" +
                "    users u\n" +
                "LEFT JOIN \n" +
                "    orders o ON u.user_id = o.user_id\n" +
                "LEFT JOIN \n" +
                "    order_details od ON o.order_id = od.order_id\n" +
                "LEFT JOIN \n" +
                "    user_favorite_pizzas ufp ON u.user_id = ufp.user_id\n" +
                "LEFT JOIN \n" +
                "    pizzas p ON ufp.pizza_id = p.pizza_id\n" +
                "WHERE \n" +
                "    u.username = ?\n" +
                "GROUP BY \n" +
                "    u.user_id, u.username, u.loyalty_points, u.address;";

        List<Map<String, Object>> userDetailsList = new ArrayList<>();

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userProfile.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> userDetails = new HashMap<>();
                userDetails.put("user_id", resultSet.getInt("user_id"));
                userDetails.put("user_name", resultSet.getString("user_name"));
                userDetails.put("loyalty_points", resultSet.getInt("loyalty_points"));
                userDetails.put("address", resultSet.getString("address"));
                userDetails.put("total_orders", resultSet.getInt("total_orders"));
                userDetails.put("total_pizzas_ordered", resultSet.getInt("total_pizzas_ordered"));
                userDetails.put("favorite_pizzas_details", resultSet.getString("favorite_pizzas_details"));

                userDetailsList.add(userDetails);
            }

            if (userDetailsList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonList(Map.of("error", "No details found for the given user.")));
            }

            return ResponseEntity.ok(userDetailsList);

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Map.of("error", "An error occurred while fetching user details.")));
        }
    }

    @PostMapping("/CreditCard")
    public ResponseEntity<Map<String, String>> CreditCard(@RequestBody Map<String, Object> CreditData) {
        if (userProfile == null) {
            Map<String, String> response = Map.of("message", "User not logged in");
            return ResponseEntity.status(400).body(response);
        }

        try {
            K2458330PaymentProcessor paymentProcessor = new K2458330PaymentProcessor();
            paymentProcessor.setPaymentStrategy(new K2458330CreditCardPayment());

            K2458330Order discountedOrder = new K2558330DiscountDecorator(order);
            discountedOrder.processLoyalty(userProfile.getUserName(), order.getTotalOrderPrice(), userProfile.getLoyaltyPoints());
            LocalDate currentDate = LocalDate.now();
            Month currentMonth = currentDate.getMonth();

            if (currentMonth == Month.DECEMBER) {
                K2458330PromotionStrategy christmasPromotion = new K2458330ChristmasDiscount(order);
            } else if (currentMonth == Month.APRIL) {
                K2458330PromotionStrategy aprilPromotion = new K2458330AprilDiscount(order);
            }

            paymentProcessor.pay(order.getOrderID(), order.getDiscount(), order.getFinalPayment());

            K2458330Order loyaltyPointsOrder = new K2458330LoyaltyPointsDecorator(order);
            loyaltyPointsOrder.processLoyalty(userProfile.getUserName(), order.getFinalPayment(), userProfile.getLoyaltyPoints());

            Map<String, String> response = Map.of(
                    "message", "Payment successful\n Order ID: " + order.getOrderID() +
                            "\n without Discount: " + order.getTotalOrderPrice() +
                            "\n Paid amount: " + order.getFinalPayment() +
                            "\n Loyalty points earned: " + order.getFinalPayment() / 100
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Payment Failed. Please try again."));
        }
    }

    @PostMapping("/DigitalWallet")
    public ResponseEntity<Map<String, String>> DigitalWallet(@RequestBody Map<String, Object> Data) {
        if (userProfile == null) {
            Map<String, String> response = Map.of("message", "User not logged in");
            return ResponseEntity.status(400).body(response);
        }

        K2458330PaymentProcessor PaymentProcessor = new K2458330PaymentProcessor();
        PaymentProcessor.setPaymentStrategy(new K2458330DigitalWalletPayment());

        K2458330Order discountedOrder = new K2558330DiscountDecorator(order);
        discountedOrder.processLoyalty(userProfile.getUserName(), order.getFinalPayment(), userProfile.getLoyaltyPoints());
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        if (currentMonth == Month.DECEMBER) {
            K2458330PromotionStrategy ChristmasPromotion = new K2458330ChristmasDiscount(order);
        } else if (currentMonth == Month.APRIL) {
            K2458330PromotionStrategy NewYearPromotion = new K2458330AprilDiscount(order);
        }
        PaymentProcessor.pay(order.getOrderID(), order.getDiscount(), order.getFinalPayment());
        K2458330Order Loyalty = new K2458330LoyaltyPointsDecorator(order);
        Loyalty.processLoyalty(userProfile.getUserName(), order.getFinalPayment(), userProfile.getLoyaltyPoints());

        Map<String, String> response = Map.of("message", "Payment Done successfully \n Order ID :" + order.getFinalPayment() + "\n your price without Discount :" + order.getTotalOrderPrice() + "\n Paid amount :" + order.getFinalPayment() + "\n Loyalty points earned : " + order.getFinalPayment() / 100);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/LoyaltyPay")
    public ResponseEntity<Map<String, String>> LoyaltyPay() {
        if (userProfile == null) {
            Map<String, String> response = Map.of("message", "User not logged in");
            return ResponseEntity.status(400).body(response);
        }

        K2458330PaymentProcessor PaymentProcessor = new K2458330PaymentProcessor();
        PaymentProcessor.setPaymentStrategy(new K2458330LoyaltyPayment(userProfile.getUserName(), order.getFinalPayment()));

        K2458330Order discountedOrder = new K2558330DiscountDecorator(order);
        discountedOrder.processLoyalty(userProfile.getUserName(), order.getFinalPayment(), userProfile.getLoyaltyPoints());
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        if (currentMonth == Month.DECEMBER) {
            K2458330PromotionStrategy ChristmasPromotion = new K2458330ChristmasDiscount(order);
        } else if (currentMonth == Month.APRIL) {
            K2458330PromotionStrategy NewYearPromotion = new K2458330AprilDiscount(order);
        }
        PaymentProcessor.pay(order.getOrderID(), order.getDiscount(), order.getFinalPayment());

        Map<String, String> response = Map.of("message", ("Payment Done successfully \n Order ID :" + order.getFinalPayment() + "\n Discount :" + discountedOrder.getDiscount() + "\n Paid amount :" + order.getFinalPayment()));
        return ResponseEntity.ok(response);

    }

    @PostMapping("/order")
    public ResponseEntity<Map<String, String>> makeOrder(@RequestBody Map<String, Object> OrderData) {
        System.out.println("Making Order");
        userProfile.updateLoyeltyPoints();
        List<Map<String, Object>> pizzas = (List<Map<String, Object>>) OrderData.get("pizzas");

        int totalPrice = pizzas.stream().mapToInt(pizza -> (int) pizza.get("price")).sum();
        String insertOrderQuery = "INSERT INTO `orders`(`user_id`, `order_date`, `total_price`, `rating`, `state`) VALUES ((select user_id from users where username= ?), ?, ?, ?, ?)";

        // Ensure userProfile is not null
        if (userProfile == null) {
            Map<String, String> response = Map.of("message", "User not logged in");
            return ResponseEntity.status(400).body(response);
        }

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Use userProfile.getUserName() for setting the parameter
            orderStatement.setString(1, userProfile.getUserName());
            orderStatement.setString(2, LocalDateTime.now().toString());
            orderStatement.setInt(3, totalPrice);
            orderStatement.setInt(4, 0); // Default rating (0)
            orderStatement.setString(5, "pending"); // Default state

            orderStatement.executeUpdate();

            int orderId = -1;
            try (var rs = orderStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }
            order.setOrderID(orderId);


            String insertOrderDetailQuery = "INSERT INTO `order_details`(`order_detail_id`, `order_id`, `pizzaName`, `price`) VALUES (NULL, ?, ?, ?)";
            try (PreparedStatement orderDetailStatement = connection.prepareStatement(insertOrderDetailQuery)) {
                for (Map<String, Object> pizza : pizzas) {
                    String pizzaName = (String) pizza.get("name");
                    int pizzaPrice = (int) pizza.get("price");

                    orderDetailStatement.setInt(1, orderId);
                    orderDetailStatement.setString(2, pizzaName);
                    orderDetailStatement.setInt(3, pizzaPrice);
                    orderDetailStatement.addBatch();
                }
                // Execute batch insert for order details
                orderDetailStatement.executeBatch();
            }

            // Return success message
            Map<String, String> response = Map.of("message", "Order placed successfully");
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            e.printStackTrace();
            Map<String, String> response = Map.of("message", "Error occurred while placing the order");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/PickUP")
    public ResponseEntity<Map<String, String>> handlePickUp() {
        order.setOrderStrategy(new K2458330PickupStrategy());
        System.out.println("Press 'ok' to process the order step by step.");

        // Loop to process the order
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("ok")) {
                String[] steps = {"prepare", "pack", "ready for pickup"};
                for (String step : steps) {
                    System.out.printf("Press 'ok' to mark order as '%s'.%n", step);
                    input = scanner.nextLine().trim().toLowerCase();

                    if (input.equals("ok")) {
                        order.processOrder();
                    } else {
                        System.out.printf("Order not marked as '%s'. Stopping process.%n", step);
                        Map<String, String> response = Map.of("Message", "Your Order is not delivered");
                        return ResponseEntity.ok(response);
                    }
                }

                break;
            } else {
                System.out.println("Invalid input. Press 'ok' to proceed.");
            }
        }
        Map<String, String> response = Map.of("message", "Your Order is delivered");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Delivery")
    public ResponseEntity<Map<String, String>> handleDelevery() {
        order.setAddress(userProfile.getAddress());
        order.setOrderStrategy(new K2458330DeliveryStrategy());
        System.out.println("Press 'ok' to process the order .");
        order.processOrder();
        Map<String, String> response = Map.of("message", ("Order is Delivered to the location : " + userProfile.getAddress()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getOrders")
    public List<Map<String, Object>> getUserOrders() {
        List<Map<String, Object>> responseList = new ArrayList<>();
        Connection connection = null;

        try {
            connection = DataBaseConnection.connect();
            if (connection == null) {
                throw new SQLException("Failed to connect to database.");
            }

            String query = "SELECT o.order_id, o.order_date, o.total_price, o.rating, o.state, od.pizzaName, od.price " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.user_id " +
                    "JOIN order_details od ON o.order_id = od.order_id " +
                    "WHERE u.username = ?";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, userProfile.getUserName());

            ResultSet rs = stmt.executeQuery();

            Map<Integer, Map<String, Object>> orderMap = new HashMap<>();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");

                if (!orderMap.containsKey(orderId)) {
                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("order_id", orderId);
                    orderData.put("order_date", rs.getString("order_date"));
                    orderData.put("total_price", rs.getDouble("total_price"));
                    orderData.put("rating", rs.getInt("rating"));
                    orderData.put("state", rs.getString("state"));

                    // Initialize pizza details list
                    orderData.put("pizzaDetails", new ArrayList<Map<String, Object>>());
                    orderMap.put(orderId, orderData);
                }

                Map<String, Object> pizzaData = new HashMap<>();
                pizzaData.put("pizzaName", rs.getString("pizzaName"));
                pizzaData.put("price", rs.getDouble("price"));

                List<Map<String, Object>> pizzaList = (List<Map<String, Object>>) orderMap.get(orderId).get("pizzaDetails");
                pizzaList.add(pizzaData);
            }

            responseList.addAll(orderMap.values());
            bubbleSortDescending(responseList);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                DataBaseConnection.disconnect();
            }
        }

        return responseList;
    }

    @GetMapping("/getPizza")
    public List<Map<String, Object>> getUserPizza() {
        List<Map<String, Object>> responseList = new ArrayList<>();

        String query = "SELECT p.pizza_id, p.name AS pizza_name, p.crust, p.sauce, p.cheese, p.toppings, p.price, p.rating, " +
                "CASE WHEN uf.user_id IS NOT NULL THEN 'Favorite' " +
                "WHEN p.pizza_type = 'predefined' THEN 'Predefined' ELSE 'User Defined' END AS pizza_category " +
                "FROM pizzas p " +
                "LEFT JOIN user_favorite_pizzas uf ON p.pizza_id = uf.pizza_id " +
                "LEFT JOIN users u ON uf.user_id = u.user_id " +
                "WHERE u.username = ? OR p.pizza_type = 'predefined';";

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (connection == null) {
                throw new SQLException("Failed to connect to database.");
            }

            // Set query parameter
            stmt.setString(1, userProfile.getUserName());

            // Execute query
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> pizzaData = new HashMap<>();
                    pizzaData.put("pizza_id", rs.getInt("pizza_id"));
                    pizzaData.put("pizza_name", rs.getString("pizza_name"));
                    pizzaData.put("crust", rs.getString("crust"));
                    pizzaData.put("sauce", rs.getString("sauce"));
                    pizzaData.put("cheese", rs.getString("cheese"));
                    pizzaData.put("toppings", rs.getString("toppings"));
                    pizzaData.put("price", rs.getDouble("price"));
                    pizzaData.put("rating", rs.getDouble("rating"));
                    pizzaData.put("pizza_category", rs.getString("pizza_category"));
                    responseList.add(pizzaData);
                }
            }

            // Sort the response list (if necessary)
            bubbleSortAscending(responseList);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responseList;
    }

    @GetMapping("/getTopPizza")
    public List<Map<String, Object>> getTopPizza() {

        K2458330RecommendationSystem recomendationSystem = new K2458330RecommendationSystem();
        List responseList = recomendationSystem.checkDb();

        return responseList;
    }

    private void bubbleSortAscending(List<Map<String, Object>> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                double price1 = (int) list.get(j).get("pizza_id");
                double price2 = (int) list.get(j + 1).get("pizza_id");
                if (price1 > price2) { // Swap if the current element is greater than the next
                    Map<String, Object> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    private void bubbleSortDescending(List<Map<String, Object>> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                double price1 = (int) list.get(j).get("order_id");
                double price2 = (int) list.get(j + 1).get("order_id");
                if (price1 < price2) {
                    // Swap
                    Map<String, Object> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    public String makePizza(K2458330PizzaBuilder builder, String name, String crustType, String sauceType, String cheeseType, List<String> toppingsList) {
        // Select Crust
        K2458330ContextCrust crustContext = switch (crustType.toLowerCase()) {
            case "thin crust" -> new K2458330ContextCrust(new K2458330ThinCrust());
            case "thick crust" -> new K2458330ContextCrust(new K2458330ThickCrust());
            case "gluten-free crust" -> new K2458330ContextCrust(new K2458330GlutenFreeCrust());
            default -> throw new IllegalArgumentException("Invalid crust type: " + crustType);
        };

        String crust = crustContext.SelectCrust();
        int crustPrice = crustContext.SelectCrustPrice();

        // Select Sauce
        K2458330ContextSauce sauceContext = switch (sauceType.toLowerCase()) {
            case "tomato sauce" -> new K2458330ContextSauce(new K2458330TomatoSauce());
            case "bbq sauce" -> new K2458330ContextSauce(new K2458330BBQSauce());
            default -> throw new IllegalArgumentException("Invalid sauce type: " + sauceType);
        };

        String sauce = sauceContext.SelectSauce();
        int saucePrice = sauceContext.SelectSaucePrice();

        // Select Cheese
        K2458330CheeseContext cheeseContext = switch (cheeseType.toLowerCase()) {
            case "mozzarella cheese" -> new K2458330CheeseContext(new K2458330MozzarellaCheese());
            case "cheddar cheese" -> new K2458330CheeseContext(new K2458330CheddarCheese());
            case "parmesan cheese" -> new K2458330CheeseContext(new K2458330ParmesanCheese());
            default -> throw new IllegalArgumentException("Invalid cheese type: " + cheeseType);
        };

        String cheese = cheeseContext.SelectCheese();
        int cheesePrice = cheeseContext.SelectCheesePrice();

        // Process Toppings
        K2458330ToppingsImpl toppingsImpl = new K2458330ToppingsImpl();
        for (String topping : toppingsList) {
            toppingsImpl.addTopping(topping);
        }
        int toppingsCost = toppingsImpl.getTotalToppingCost();

        // Build Pizza
        builder.setName(name)
                .setCrust(crust, crustPrice)
                .setSauce(sauce, saucePrice)
                .setCheese(cheese, cheesePrice)
                .setToppings(toppingsImpl.getToppings(), toppingsCost);

        // Optionally, print the pizza details for debugging
        String x = (String.format(
                "Pizza Name: %s \n Crust: %s ($%d)\n Sauce: %s ($%d)\n Cheese: %s ($%d)\n Toppings: %s (Total Cost: $%d)\n Total pizza price: $%d",
                builder.getName(), crust, crustPrice, sauce, saucePrice, cheese, cheesePrice, toppingsImpl.getToppings(), toppingsCost, (crustPrice + saucePrice + cheesePrice + toppingsCost)
        ));
        this.pizzaPrice = (crustPrice + saucePrice + cheesePrice + toppingsCost);

        return x;
    }

    public String makeFavPizza(K2458330PizzaBuilder builder, String name, String crustType, String sauceType, String cheeseType, List<String> toppingsList) {
        // Select Crust
        K2458330ContextCrust crustContext = switch (crustType.toLowerCase()) {
            case "thin crust" -> new K2458330ContextCrust(new K2458330ThinCrust());
            case "thick crust" -> new K2458330ContextCrust(new K2458330ThickCrust());
            case "gluten-free crust" -> new K2458330ContextCrust(new K2458330GlutenFreeCrust());
            default -> throw new IllegalArgumentException("Invalid crust type: " + crustType);
        };

        String crust = crustContext.SelectCrust();
        int crustPrice = crustContext.SelectCrustPrice();

        // Select Sauce
        K2458330ContextSauce sauceContext = switch (sauceType.toLowerCase()) {
            case "tomato sauce" -> new K2458330ContextSauce(new K2458330TomatoSauce());
            case "bbq sauce" -> new K2458330ContextSauce(new K2458330BBQSauce());
            default -> throw new IllegalArgumentException("Invalid sauce type: " + sauceType);
        };

        String sauce = sauceContext.SelectSauce();
        int saucePrice = sauceContext.SelectSaucePrice();

        // Select Cheese
        K2458330CheeseContext cheeseContext = switch (cheeseType.toLowerCase()) {
            case "mozzarella cheese" -> new K2458330CheeseContext(new K2458330MozzarellaCheese());
            case "cheddar cheese" -> new K2458330CheeseContext(new K2458330CheddarCheese());
            case "parmesan cheese" -> new K2458330CheeseContext(new K2458330ParmesanCheese());
            default -> throw new IllegalArgumentException("Invalid cheese type: " + cheeseType);
        };

        String cheese = cheeseContext.SelectCheese();
        int cheesePrice = cheeseContext.SelectCheesePrice();

        // Process Toppings
        K2458330ToppingsImpl toppingsImpl = new K2458330ToppingsImpl();
        for (String topping : toppingsList) {
            toppingsImpl.addTopping(topping);
        }
        int toppingsCost = toppingsImpl.getTotalToppingCost();

        // Build Pizza
        builder.setName(name)
                .setCrust(crust, crustPrice)
                .setSauce(sauce, saucePrice)
                .setCheese(cheese, cheesePrice)
                .setToppings(toppingsImpl.getToppings(), toppingsCost);
        builder.save(name, crust, sauce, cheese, ((toppingsImpl.getToppings()).toString()), (crustPrice + saucePrice + cheesePrice + toppingsCost), 0.0, "user");
        userProfile.SaveFavoritePizza(name);

        // Optionally, print the pizza details for debugging
        String x = (String.format(
                "Pizza Name: %s \n Crust: %s ($%d)\n Sauce: %s ($%d)\n Cheese: %s ($%d)\n Toppings: %s (Total Cost: $%d)\n Total pizza price: $%d",
                builder.getName(), crust, crustPrice, sauce, saucePrice, cheese, cheesePrice, toppingsImpl.getToppings(), toppingsCost, (crustPrice + saucePrice + cheesePrice + toppingsCost)
        ));

        return x;
    }


}
