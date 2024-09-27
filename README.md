# Trading-Demo

## Overview
**Trading-Demo** is a demo crypto trading platform that allows users to sign up, securely authenticate using JWT tokens, and enable Two-Factor Authentication (TFA) via email. The platform provides the ability to search for cryptocurrencies by keyword or ID, view detailed information about specific coins, manage a personal wallet, and create orders to buy or sell coins. IF you choose not to enable TFA you will only be able to search for crypto coins and all the other functionalities won't be available to you.

## Features
- **User SignUp & SignIn**: Create a new account and securely sign in.
- **JWT-Based Security**: Authentication is managed using JWT tokens.
- **Enable OTP**: Two-Factor Authentication (TFA) via email.
- **Crypto Search**: Search for crypto coins by keyword or ID and get detailed information.
- **Wallet Management**: A wallet is automatically created upon sign-up and TFA activation. Users can deposit, withdraw, buy, sell, and transfer currency from their wallets.
- **Create Orders**: Users can create orders to buy or sell coins within the platform.

## Prerequisites
Ensure the following are installed on your system:
- **Java JDK** (Version 17 or higher)
- **Spring Boot** (Version 3.2.9) - Spring Boot is the framework used for building the application
- **Maven** for project build and dependency management.
- **MySQL**
- **Postman** (or another API testing tool) for testing API endpoints.
  
### Installing Postman
1. Download Postman from the [official website](https://www.postman.com/downloads/).
2. Follow the installation steps for your operating system.

## Running the Project
- Navigate to the project directory:

      cd Trading-Demo
- Build and run the project:

      mvn clean install
      mvn exec:java

## Workflow
This section explains the high-level workflow of the key services in the **Trading-Demo** platform.

### 1. User Registration and Authentication
- A user signs up and authenticates using **JWT tokens**.
- Send an OTP via email in order to use it to enable the Two-Factor Authentication
- Two-Factor Authentication (TFA) should be enabled via email using the **UserService**.
- Once authenticated, a **wallet** is automatically created for the user by the **WalletService**.

### 2. Crypto Search
- Get crypto coin by keyword
- Get crypto coin by market cap
- Get crypto price chart(for days/weeks/months)
- Get trending crypto coins

### 3. Creating an Order (Buy/Sell)
When a user initiates a buy or sell operation, the following steps occur:

#### Buy Order Workflow
1. The user requests to buy an asset.
2. The **OrderService** creates a new `Order` and `OrderItem` using the current price of the coin and the quantity requested.
3. **WalletService** is called to process the payment, deducting the amount from the user's wallet.
4. **AssetService** checks if the user already owns the asset (cryptocurrency):
    - If yes, the quantity of the asset is updated.
    - If not, a new asset is created for the user.
5. The order is marked as **COMPLETED**, and the `Order` is saved.

#### Sell Order Workflow
1. The user requests to sell an asset.
2. **AssetService** checks if the user has enough of the asset to sell:
    - If the user has sufficient quantity, the **OrderService** creates the sell order.
    - If not, the order is rejected.
3. **WalletService** is called to transfer the sale amount to the user's wallet.
4. The asset quantity is updated or deleted if all the assets are sold.
5. The order is marked as **COMPLETED** and saved.

### 4. Payment Workflow
- Payments are processed via the **WalletService**.
- For buy orders, the wallet balance is deducted.
- For sell orders, the wallet balance is increased.

### 5. Asset Management
- The **AssetService** is responsible for managing user assets (cryptocurrencies).
- It ensures that the correct quantities are updated during buy and sell transactions.
- Assets can be created, updated, or deleted based on user actions.

## Testing with Postman
- Open Postman.
- Create a new request and set the request type (GET, POST, etc.).
- Enter the API endpoint URL (e.g., http://localhost:2120/auth/signup).
- For POST requests, add the JSON body under the Body tab and select raw and JSON format.
- Click Send to test the API.

## Example Requests Using Postman:

### Sign Up (POST request):
- URL: http://localhost:2120/auth/signup
- Body (JSON):
{
    "fullName":"yourname",
    "email":"youremail@mail.com",
    "password":"yourpassword"
}

### Send OTP (POST request):
- URL: http://localhost:2120/api/users/verification/send-otp?verification_type=email
- Auth Type: Bearer Token as Environmental variable(you can get it from the response when you sign up)
- Query Param: verification_type

### Enable TFA (PATCH request):
- URL: http://localhost:2120/api/users/enable-tfa/verify-opt/{{OTP}}
- Auth Type: Bearer Token
- Env variable: OTP(you will receive it via email when you use the send OTP service)

### Search for a Crypto by keyword (GET request):
- URL: http://localhost:2120/coins/search?keyword=solana
- Query param: keyword

### Get Wallet Info (GET request):
- URL: http://localhost:2120/api/wallet
- Auth Type: Bearer Token

### Wallet Deposit (PUT request):
- URL: http://localhost:2120/api/wallet/1/deposit?amount=1000
- Auth Type: Bearer Token
- Query Param: amount
  
### Create Order (POST request):
- URL: http://localhost:2120/api/orders/pay
- Auth Type: Bearer Token
- Body (JSON):
{
    "coinId" : "solana",
    "quantity" : 1.5,
    "orderType" : "buy"
}

### Get User Orders (GET request):
- URL: http://localhost:2120/api/orders?orderType=buy&assetSymbol=sol
- Auth Type: Bearer Token
- Query Params: orderType & assetSymbol


## Project Structure: 
      src/
      └── main/
         └── java/
           └── com/stan/cryptoTrading/
              └── config/          # Configuration files (JWT, Security, etc.)
              └── controller/      # REST controllers for handling API requests
              └── model/           # Data models (User, Wallet, Coin, Order, etc.)
              └── repository/      # Interfaces for database operations
              └── response/        # Custom response objects for API responses
              └── service/         # Service layer that handles business logic
              └── utils/           # Utility classes (helpers for common operations)
      └── test/                    # Unit and integration tests
