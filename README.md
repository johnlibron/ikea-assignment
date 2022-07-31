# Warehouse Software

## Overview

This warehouse software holds articles, and the articles contain an identification number, a name, and available stock. It loads the articles into the software from a file, see the attached `inventory.json`. The warehouse software also has products, products are made of different articles. Products have a name, price, and a list of articles from which they are made with a quantity. The products also are loaded from a file, see the attached `products.json`.

## How to use

- Install IntelliJ IDEA, MySQL, and Postman API in your local environment
- Open the project to the IntelliJ IDEA
- Reload the maven repository of the project
- Access MySQL CLI and create username, password, and database.
- Change the username, password, and database name in the `application.properties`
- Edit the `Run Warehouse Configuration` and put program CLI arguments `products.json inventory.json` to load the data
- If the arguments are not specified in the program CLI, the application will load the `test-products.json` and `test-inventory.json` instead.
- Run the `WarehouseApplication`
- Access the REST API endpoints through Postman API
- To test the current functionality, run the `ProductControllerTest` and `ProductServiceTest`

## Current functionality
* Get all products and quantity of each that is available with the current inventory
```sh
GET http://localhost:8080/api/v1/products/available

Status 200: Available products were returned

Response: List<AvailableProductDto>
[
  {
    "name": "Dining Chair",
    "quantity": 2
  },
  {
    "name": "Dining Table",
    "quantity": 1
  }
]
```
The logic of `ProductController.getAvailableProducts()` is to iterate all the products and then get the minimum quantity of each product that is available with the current inventory. The product will not be available if the inventory stock is less than the required stock amount because the product cannot be formed if it has insufficient inventory.

* Purchase a product and update the inventory accordingly
```sh
PUT http://localhost:8080/api/v1/products/{productName}

Status 200: Product was purchased and Inventory was updated
       404: Product/Inventory not found
       409: Insufficient inventory

Response: AvailableProductDto
{
  "name": "Dining Chair",
  "quantity": 1
}
```
The logic of `ProductController.purchaseProduct(productName)` is to check first the parameter `productName` if the product exists in the product list and if the product articles exist in the inventory. Check the current inventory if it has available stocks to purchase the product then update/subtract the stock with the required stock amount.

## For the next iteration

- Add a new article for a specific product
- Change/update the amount of stock needed for a specific product article
- Add new inventory and products with articles
- Update stocks in the current inventory