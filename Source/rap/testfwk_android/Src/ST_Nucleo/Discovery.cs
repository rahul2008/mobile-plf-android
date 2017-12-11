using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ST_Nucleo
{

    public class Discovery
    {

        private Random randNumGenerator = new Random(100000);

        private List<DiscoveredClient> discoveredClients = new List<DiscoveredClient>();

        public Discovery()
        {

        }

        public Discovery(Dictionary<string, string> discoveryData)
        {
            for (int i = 0; i < discoveryData.Count / 7; i++)
            {
                Dictionary<string, dynamic> advData;
                DiscoveredClient client = new DiscoveredClient();
                client.Name = discoveryData["NameDevice" + i.ToString()];
                client.Address = Int64.Parse((discoveryData["Address" + i.ToString()]));
                client.AddressType = Int32.Parse((discoveryData["AddressType" + i.ToString()]));
                client.TypeEvent = (AdvertisingType)Int32.Parse((discoveryData["EventType" + i.ToString()]));
                client.DataLenght = Int32.Parse((discoveryData["DataLenght" + i.ToString()]));
                client.Data = FromStringToArrayData(discoveryData["Data" + i.ToString()]);
                client.RSSI = Int32.Parse(discoveryData["RSSI" + i.ToString()]);

                if (client.TypeEvent == AdvertisingType.ADV_IND)
                    advData = decodeAdvData(client.Data);

                List<DiscoveredClient> intersect =
                    discoveredClients.Where(x => x.Data.ToArray().SequenceEqual(client.Data.ToArray())).ToList();

                if (!intersect.Any())
                {
                    discoveredClients.Add(client);
                }

            }
        }

        public List<DiscoveredClient> DiscoveredClients { get { return discoveredClients; } }

        public Dictionary<string, dynamic> decodeAdvData(IEnumerable<int> data)
        {
            IEnumerator<int> enumerator = data.GetEnumerator();
            enumerator.Reset();

            Dictionary<string, dynamic> advData = new Dictionary<string, dynamic>();

            while (enumerator.MoveNext())
            {
                int length = enumerator.Current;
                enumerator.MoveNext();
                int type = enumerator.Current;

                List<int> dataSection = new List<int>();
                for (int i = 0; i < length - 1; i++)
                {
                    enumerator.MoveNext();
                    dataSection.Add(enumerator.Current);
                }

                Dictionary<string, dynamic> singlePacket = AnalyzeData(type, dataSection);
                advData = advData.Concat(singlePacket).ToDictionary(s => s.Key, s => s.Value);

            }

            return advData;

        }


        private Dictionary<string, dynamic> AnalyzeData(int dataType, List<int> data)
        {
            Dictionary<string, dynamic> dataDecoded = new Dictionary<string, dynamic>();
            string type = "";

            switch (dataType)
            {
                case 0x01:
                    type = "Flags";
                    dataDecoded[type] = data;
                    break;

                case 0x08:
                case 0x09:
                    type = "Local_Name";
                    dataDecoded[type] = data;
                    break;

                case 0x02:
                case 0x03:
                    type = "Service_UUID_16";
                    dataDecoded[type] = GetServicesUUID16bits(data);
                    break;

                case 0x06:
                case 0x07:
                case 0x11:
                    type = "Service_UUID_128";
                    dataDecoded[type] = GetServicesUUID128bits(data);
                    break;

                case 0xFF:
                    type = "Manifacturer_Specific_Data";
                    dataDecoded[type] = data;
                    break;

                default:

                    type = "Not_Encoder" + randNumGenerator.Next().ToString();
                    dataDecoded[type] = data;
                    break;

            }

            return dataDecoded;
        }


        private IEnumerable<int> FromStringToArrayData(string data)
        {
            var charsToRemove = new string[] { "[", "]", " " };
            foreach (var c in charsToRemove) { data = data.Replace(c, string.Empty); }
            string[] dataSplit = data.Split(',');
            return dataSplit.Select(x => Int32.Parse(x)).ToArray();
        }



        private List<int> GetServicesUUID16bits(List<int> adv)
        {
            List<int> servicesUUID = new List<int>();
            int index = 0;

            for (int i = 0; i < adv.Count / 2; i++)        //16bitUUID are split in 2 locations
            {
                int UUID_part1 = adv[i * 2];
                int UUID_part2 = adv[i * 2 + 1] << 8;
                index++;
                servicesUUID.Add(UUID_part2 + UUID_part1);
            }

            return servicesUUID;
        }

        private List<string> GetServicesUUID128bits(List<int> adv)
        {
            List<string> servicesUUID = new List<string>();

            ulong UUIDaccumulator = 0;
            int index = 0;
            string serviceUUID128 = "";
            for (int i = 0; i < adv.Count; i++)
            {
                serviceUUID128 = serviceUUID128 + adv[i].ToString("X");
                if (index % 15 == 0 && index != 0)
                {
                    servicesUUID.Add(serviceUUID128);
                    serviceUUID128 = "";
                    index = 0;
                }
                index++;
            }

            return servicesUUID;
        }
    }


    public struct DiscoveredClient
    {
        public long Address;
        public string Name;
        public int AddressType;
        public AdvertisingType TypeEvent; //Adv type
        public int DataLenght;
        public IEnumerable<int> Data;
        public int RSSI;
    }

    public struct ADV_Flag
    {
        public bool LELimitedDiscoverableMode;
        public bool LEGeneralDiscoverableMode;
        public bool BR_EDR_NotSupported;
        public bool SimultaneousLEandBR_EDR_Controller;
        public bool SimultaneousLEandBR_EDR_Host;
        public bool Reserved1;
        public bool Reserved2;
        public bool Reserved3;
    }

}
