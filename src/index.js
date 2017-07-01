var faker = require('faker');

exports.handler = function(event, context){
  // return book details for a given book
  var book = {};
  // name, address, city, state, phone, book date, ship date, price, transaction type
  book.id = event.bookId;
  book.name = getName();
  book.address = getShippingAddress();
  book.city = getShippingCity();
  book.state = getShippingState();
  book.phone = getPhone();
  book.shipMethod = getShipMethod();
  book.price = getPrice();
  context.succeed(book);
}

function getName() {
  return faker.name.findName();
}

function getShippingAddress() {
  return faker.address.streetAddress() + " " + faker.address.streetSuffix();
}

function getShippingCity() {
  return faker.address.city();
}

function getShippingState() {
  return faker.address.state();
}

function getPhone() {
  return faker.phone.phoneNumber();
}

function getShipMethod() {
  var shippers = ['FedEx', 'UPS', 'USPS', 'DHL']
  return shippers[Math.floor(Math.random() * 4)];
}

function getPrice() {
  return faker.commerce.price();
}
