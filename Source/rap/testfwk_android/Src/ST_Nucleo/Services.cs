using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ST_Nucleo
{
    public class Services
    {

        private List<int> handlesUUID = new List<int>();
        private List<int> startHandler = new List<int>();
        private List<int> endHandleService = new List<int>();

        public Services(List<int> handlersUUID, List<int> startHandler, List<int> endHandler)
        {
            this.handlesUUID = handlersUUID;
            this.startHandler = startHandler;
            this.endHandleService = endHandler;
        }

        public List<int> HandlesUUID { get { return handlesUUID; } }
        public List<int> StartHandler { get { return startHandler; } }
        public List<int> EndHandler
        {
            get { return endHandleService; }

        }
    }
}
