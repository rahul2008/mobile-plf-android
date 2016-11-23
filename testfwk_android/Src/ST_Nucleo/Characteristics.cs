using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ST_Nucleo
{
    public class Characteristics
    {
        private List<int> handleChar = new List<int>();
        private List<int> uuidChar = new List<int>();
        private List<int> valueHandlerChar = new List<int>();
        private List<int> attributes = new List<int>();

        public Characteristics(List<int> handlersUUID, List<int> uuidChar, List<int> valueHandlerChar, List<int> attributes)
        {
            this.handleChar = handlersUUID;
            this.uuidChar = uuidChar;
            this.valueHandlerChar = valueHandlerChar;
            this.attributes = attributes;
        }

        public List<int> HndleChar { get { return handleChar; } }
        public List<int> UUIDChar { get { return uuidChar; } }
        public List<int> ValueHandlerChar { get { return valueHandlerChar; } }
        public List<int> Attributes
        {
            get { return attributes; }
        }

    }
}
