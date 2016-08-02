using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Stock).
    /// </summary>
    public class StockElement
    {
        public int stockLevel { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Product).
    /// </summary>
    public class ProductElement
    {
        public bool availableForPickup { get; set; }
        public string code { get; set; }
        public bool purchasable { get; set; }
        public StockElement stock { get; set; }
        public string url { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(TotalPrice).
    /// </summary>
    public class TotalPriceElement
    {
        public string currencyIso { get; set; }
        public double value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Entry).
    /// </summary>
    public class Entry
    {
        public int entryNumber { get; set; }
        public ProductElement product { get; set; }
        public int quantity { get; set; }
        public TotalPriceElement totalPrice { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(TotalPriceWithTax).
    /// </summary>
    public class TotalPriceWithTax
    {
        public string currencyIso { get; set; }
        public double value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Cart).
    /// </summary>
    public class Cart
    {
        public string code { get; set; }
        public List<Entry> entries { get; set; }
        public string guid { get; set; }
        public int totalItems { get; set; }
        public TotalPriceElement totalPrice { get; set; }
        public TotalPriceWithTax totalPriceWithTax { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call for Get Cart(GET).
    /// </summary>
    public class CartList
    {
        public List<Cart> carts { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call to Get Cart Entries(GET).
    /// </summary>
    public class CartEntriesList
    {
        public List<Entry> orderEntries { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Country).
    /// </summary>
    public class CountryElement
    {
        public string isocode { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Region).
    /// </summary>
    public class Region
    {
        public string countryIso { get; set; }
        public string isocode { get; set; }
        public string isocodeShort { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Addresses).
    /// </summary>
    public class Addresses
    {
        public CountryElement country { get; set; }
        public string firstName { get; set; }
        public string formattedAddress { get; set; }
        public string id { get; set; }
        public string lastName { get; set; }
        public string line1 { get; set; }
        public string line2 { get; set; }
        public string phone { get; set; }
        public string postalCode { get; set; }
        public bool shippingAddress { get; set; }
        public string titleCode { get; set; }
        public string town { get; set; }
        public bool visibleInAddressBook { get; set; }
        public Region region { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call to get addresses(GET).
    /// </summary>
    public class AddressList
    {
        public List<Addresses> addresses { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Billing Address).
    /// </summary>
    public class BillingAddress
    {
        public Country country { get; set; }
        public string firstName { get; set; }
        public string id { get; set; }
        public string lastName { get; set; }
        public string line1 { get; set; }
        public string line2 { get; set; }
        public string postalCode { get; set; }
        public string town { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Card Type).
    /// </summary>
    public class CardType
    {
        public string code { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Payment).
    /// </summary>
    public class PaymentElement
    {
        public string accountHolderName { get; set; }
        public BillingAddress billingAddress { get; set; }
        public string cardNumber { get; set; }
        public CardType cardType { get; set; }
        public bool defaultPayment { get; set; }
        public string expiryMonth { get; set; }
        public string expiryYear { get; set; }
        public string id { get; set; }
        public string lastSuccessfulOrderDate { get; set; }
        public bool saved { get; set; }
        public string subscriptionId { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call to get payment details(GET).
    /// </summary>
    public class PaymentList
    {
        public List<PaymentElement> payments { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call to create cart details(POST).
    /// </summary>
    public class CreateCartResult
    {
        public string type { get; set; }
        public string code { get; set; }
        public string guid { get; set; }
        public int totalItems { get; set; }
        public TotalPriceElement totalPrice { get; set; }
        public TotalPriceWithTax totalPriceWithTax { get; set; }
    }

    //Post Add to cart
    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call to add cart details(POST).
    /// </summary>
    public class AddToCartResponse
    {
        public Entry entry { get; set; }
        public int quantity { get; set; }
        public int quantityAdded { get; set; }
        public string statusCode { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call to create addresses(POST).
    /// </summary>
    public class CreateAddressResponse
    {
        public CountryElement country { get; set; }
        public string firstName { get; set; }
        public string id { get; set; }
        public string lastName { get; set; }
        public string line1 { get; set; }
        public string line2 { get; set; }
        public string postalCode { get; set; }
        public string town { get; set; }
    }

    //Post Payment
    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Payment).
    /// </summary>
    public class PaymentResponse
    {
        public string worldpayUrl { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(Promotion).
    /// </summary>
    public class Promotion
    {
        public string code { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(AppliedOrderPromotion).
    /// </summary>
    public class AppliedOrderPromotion
    {
        public string description { get; set; }
        public Promotion promotion { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(DeliveryAddress).
    /// </summary>
    public class DeliveryAddress
    {
        public Country country { get; set; }
        public string firstName { get; set; }
        public string id { get; set; }
        public string lastName { get; set; }
        public string line1 { get; set; }
        public string postalCode { get; set; }
        public Region region { get; set; }
        public string town { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(DeliveryCost).
    /// </summary>
    public class DeliveryCost
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public int value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(DeliveryMode).
    /// </summary>
    public class DeliveryMode
    {
        public string code { get; set; }
        public DeliveryCost deliveryCost { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(TotalPriceWithTaxDetail).
    /// </summary>
    public class TotalPriceWithTaxDetail
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public int value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(DeliveryOrderGroup).
    /// </summary>
    public class DeliveryOrderGroup
    {
        public List<Entry> entries { get; set; }
        public TotalPriceWithTaxDetail totalPriceWithTax { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(OrderDiscounts).
    /// </summary>
    public class OrderDiscounts
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public int value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(PaymentInfo).
    /// </summary>
    public class PaymentInfo
    {
        public string accountHolderName { get; set; }
        public BillingAddress billingAddress { get; set; }
        public string cardNumber { get; set; }
        public CardType cardType { get; set; }
        public bool defaultPayment { get; set; }
        public string expiryMonth { get; set; }
        public string expiryYear { get; set; }
        public string id { get; set; }
        public bool saved { get; set; }
        public string subscriptionId { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(ProductDiscounts).
    /// </summary>
    public class ProductDiscounts
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public int value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(SubTotal).
    /// </summary>
    public class SubTotal
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public int value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(TotalDiscounts).
    /// </summary>
    public class TotalDiscounts
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public int value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API calls(TotalTax).
    /// </summary>
    public class TotalTax
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public int value { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(User).
    /// </summary>
    public class User
    {
        public string name { get; set; }
        public string uid { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(UnconsignedEntry).
    /// </summary>
    public class UnconsignedEntry
    {
        public int entryNumber { get; set; }
        public Product product { get; set; }
        public int quantity { get; set; }
        public TotalPrice totalPrice { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call to post submit order(POST).
    /// </summary>
    public class SubmitOrderResponse
    {
        public string type { get; set; }
        public List<AppliedOrderPromotion> appliedOrderPromotions { get; set; }
        public bool calculated { get; set; }
        public string code { get; set; }
        public DeliveryAddress deliveryAddress { get; set; }
        public DeliveryCost deliveryCost { get; set; }
        public int deliveryItemsQuantity { get; set; }
        public DeliveryMode deliveryMode { get; set; }
        public List<DeliveryOrderGroup> deliveryOrderGroups { get; set; }
        public List<Entry> entries { get; set; }
        public string guid { get; set; }
        public bool net { get; set; }
        public OrderDiscounts orderDiscounts { get; set; }
        public PaymentInfo paymentInfo { get; set; }
        public int pickupItemsQuantity { get; set; }
        public ProductDiscounts productDiscounts { get; set; }
        public string site { get; set; }
        public string store { get; set; }
        public SubTotal subTotal { get; set; }
        public TotalDiscounts totalDiscounts { get; set; }
        public int totalItems { get; set; }
        public TotalPrice totalPrice { get; set; }
        public TotalPriceWithTaxDetail totalPriceWithTax { get; set; }
        public TotalTax totalTax { get; set; }
        public User user { get; set; }
        public string created { get; set; }
        public bool guestCustomer { get; set; }
        public string statusDisplay { get; set; }
        public List<UnconsignedEntry> unconsignedEntries { get; set; }
    }

    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call(DeliveryModeDetail).
    /// </summary>
    public class DeliveryModeDetail
    {
        public string code { get; set; }
        public DeliveryCost deliveryCost { get; set; }
        public string description { get; set; }
        public string name { get; set; }
    }

    //Get Delivery Modes
    /// <summary>
    /// This class is a POJO used to deserialize JSON responses recieved from REST API call to get delivery modes(GET).
    /// </summary>
    public class DeliveryModeList
    {
        public List<DeliveryModeDetail> deliveryModes { get; set; }
    }

    public class SearchCategoriesList
    {
        public List<SearchCategories> searchCategoriesList { get; set; }
    }

    public class SearchCategories
    {
        public string type { get; set; }
        public CurrentQuery currentQuery { get; set; }
        public string freeTextSearch { get; set; }
        public Pagination pagination { get; set; }
        public List<ProductSearch> products { get; set; }
        public List<Sort> sorts { get; set; }
    }

    public class CurrentQuery
    {
        public Query query { get; set; }
        public string url { get; set; }
    }

    public class Query
    {
        public string value { get; set; }
    }

    public class Pagination
    {
        public int currentPage { get; set; }
        public int pageSize { get; set; }
        public string sort { get; set; }
        public int totalPages { get; set; }
        public int totalResults { get; set; }
    }

    public class ProductSearch
    {
        public bool availableForPickup { get; set; }
        public string code { get; set; }
        public DiscountPrice discountPrice { get; set; }
        public string name { get; set; }
        public Price price { get; set; }
        public PriceRange priceRange { get; set; }
        public StockDetails stock { get; set; }
        public string url { get; set; }
        public bool purchasable { get; set; }
    }

    public class Sort
    {
        public string code { get; set; }
        public bool selected { get; set; }
    }

    public class DiscountPrice
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public double value { get; set; }
    }

    public class Price
    {
        public string currencyIso { get; set; }
        public string formattedValue { get; set; }
        public string priceType { get; set; }
        public double value { get; set; }
    }

    public class PriceRange
    {
        public string price { get; set; }
    }

    public class StockDetails
    {
        public string stockLevelStatus { get; set; }
        public int stockLevel { get; set; }
    }

}
