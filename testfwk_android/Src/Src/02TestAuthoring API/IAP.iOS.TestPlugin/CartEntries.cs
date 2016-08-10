using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    class CartEntries
    {

    }

    public class Stock
    {
        public int stockLevel { get; set; }
    }

    public class Product
    {
        public bool availableForPickup { get; set; }
        public string code { get; set; }
        public bool purchasable { get; set; }
        public Stock stock { get; set; }
        public string url { get; set; }
    }

    public class TotalPrice
    {
        public string currencyIso { get; set; }
        public double value { get; set; }
    }

    public class OrderEntry
    {
        public int entryNumber { get; set; }
        public Product product { get; set; }
        public int quantity { get; set; }
        public TotalPrice totalPrice { get; set; }
    }

    public class RootObject
    {
        public List<OrderEntry> orderEntries { get; set; }
    }
    
  
}

