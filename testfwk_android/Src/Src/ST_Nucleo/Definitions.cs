using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ST_Nucleo
{

    public enum AdvertisingFilter
    {
        NO_WHITE_LIST_USE = 0x00,           // Process scan and connection requests from all devices (i.e., the White List is not in use) */
        WHITE_LIST_FOR_ONLY_SCAN = 0x01,    // Process connection requests from all devices and only scan requests from devices that are in the White List */
        WHITE_LIST_FOR_ONLY_CONN = 0x02,    //Process scan requests from all devices and only connection requests from devices that are in the White List */
        WHITE_LIST_FOR_ALL = 0x03           //<Process scan and connection requests only from devices in the White List. */
    }

    public enum AddressType
    {
        PUBLIC_ADDR = 0x00,
        RANDOM_ADDR = 0x01,
        STATIC_RANDOM_ADDR = 0x01,
        RESOLVABLE_PRIVATE_ADDR = 0x02,
        NON_RESOLVABLE_PRIVATE_ADDR = 0x03
    }

    public enum DirectedAdvertisingTypes
    {
        HIGH_DUTY_CYCLE_DIRECTED_ADV = 1,
        LOW_DUTY_CYCLE_DIRECTED_ADV = 4
    }

    public enum AdvertisingType
    {
        ADV_IND = 0x00,
        ADV_DIRECT_IND = 0x01,
        ADV_SCAN_IND = 0x02,
        ADV_NONCONN_IND = 0x03,
        SCAN_RSP = 0x04
    }

    public enum AdvertisingRanges
    {
        ADV_INTERVAL_LOWEST_CONN = 0x0020,  //lowest allowed interval value for connectable types(20ms)..multiple of 625us
        ADV_INTERVAL_HIGHEST = 0x4000,  //highest allowed interval value (10.24s)..multiple of 625us.
        ADV_INTERVAL_LOWEST_NONCONN = 0x00A0   //lowest allowed interval value for non connectable types (100ms)..multiple of 625us.
    }

    public enum AdvertisingChannels
    {
        ADV_CH_37 = 0x01,
        ADV_CH_38 = 0x02,
        ADV_CH_39 = 0x04
    }

    public enum ScanTypes
    {
        PASSIVE_SCAN = 0,
        ACTIVE_SCAN = 1

    }


    public enum IOCapabilities
    {
        IO_CAP_DISPLAY_ONLY = 0x00,
        IO_CAP_DISPLAY_YES_NO = 0x01,
        IO_CAP_KEYBOARD_ONLY = 0x02,
        IO_CAP_NO_INPUT_NO_OUTPUT = 0x03,
        IO_CAP_KEYBOARD_DISPLAY = 0x04,
    }

    public enum HighPowerTX
    {
        ENABLED = 0,
        DISABLED = 1
    }

    public enum PALevelTX
    {
        LEVEL_0 = 0,        // HighPower ON = -18   dbm -  HighPower OFF = -15    dbm   
        LEVEL_1 = 1,        // HighPower ON = -14.7 dbm -  HighPower OFF = -11.7  dbm    
        LEVEL_2 = 2,        // HighPower ON = -11.4 dbm -  HighPower OFF = -8.4   dbm
        LEVEL_3 = 3,        // HighPower ON = -8.1  dbm -  HighPower OFF = -5.1   dbm
        LEVEL_4 = 4,        // HighPower ON = -4.9  dbm -  HighPower OFF = -2.1   dbm
        LEVEL_5 = 5,        // HighPower ON = -1.6  dbm -  HighPower OFF = +1.4   dbm
        LEVEL_6 = 6,        // HighPower ON = +1.7  dbm -  HighPower OFF = +4.7   dbm
        LEVEL_7 = 7         // HighPower ON = +5.0  dbm -  HighPower OFF = +8.0   dbm
    }

    public enum AuthenticationBonding
    {
        DISABLE = 0x00,
        ENABLE = 0x01       // only if bonding is enable the bonding information is stored in flash
    }

    public enum FixedPin
    {
        USE = 0x00,
        DONTUSE = 0x01
    }

    public enum RequestBonding
    {
        ONLYIFNOTBOUNDED = 0x00,
        ALWAYS = 0x01
    }

    public enum AuthorizePairingResponse
    {
        ACCEPT = 0x01,
        REJECT = 0x02
    }

    public enum AuthRequirement
    {
        NO_BONDING = 0x00,
        BONDING = 0x01
    }

    public enum MITMRequirement
    {
        MITM_PROTECTION_NOT_REQUIRED = 0x00,
        MITM_PROTECTION_REQUIRED = 0x01
    }

    public enum OOBData //Out of Band data
    {
        OOB_AUTH_DATA_ABSENT = 0x00,
        OOB_AUTH_DATA_PRESENT = 0x01
    }

    public enum AuthorRequirement //Out of Band data
    {
        AUTHORIZATION_NOT_REQUIRED = 0x00,
        AUTHORIZATION_REQUIRED = 0x01
    }

    public enum ConnAuthorization
    {
        CONNECTION_AUTHORIZED = 0x01,
        CONNECTION_REJECTED = 0x02
    }

    public enum UseFixedPin
    {
        USE_FIXED_PIN_FOR_PAIRING = 0x00,
        DONOT_USE_FIXED_PIN_FOR_PAIRING = 0x01
    }

    public enum LinkSecurityStatus
    {
        SM_LINK_AUTHENTICATED = 0x01,
        SM_LINK_AUTHORIZED = 0x02,
        SM_LINK_ENCRYPTED = 0x04
    }

    public enum SMPPairingFailedCodes
    {
        PASSKEY_ENTRY_FAILED = 0x01,
        OOB_NOT_AVAILABLE = 0x02,
        AUTH_REQ_CANNOT_BE_MET = 0x03,
        CONFIRM_VALUE_FAILED = 0x04,
        PAIRING_NOT_SUPPORTED = 0x05,
        INSUFF_ENCRYPTION_KEY_SIZE = 0x06,
        CMD_NOT_SUPPORTED = 0x07,
        UNSPECIFIED_REASON = 0x08,
        VERY_EARLY_NEXT_ATTEMPT = 0x09,
        SM_INVALID_PARAMS = 0x0A
    }

    public enum GATTError
    {
        INVALID_HANDLE = 0x01,
        READ_NOT_PERMITTED = 0x02,
        WRITE_NOT_PERMITTED = 0x03,
        INVALID_PDU = 0x04,
        INSUFFICIENT_AUTHENTICATION = 0x05,
        REQUEST_NOT_SUPPORTED = 0x06,
        INVALID_OFFSET = 0x07,
        INSUFFICIENT_AUTH = 0x08,
        PREPARE_QUEUE_FULL = 0x09,
        ATTRIBUTE_NOT_FOUND = 0x0A,
        ATTRIBUTE_NOT_LONG = 0x0B,
        INSUFFICIENT_ENCRYP_KEY_SIZE = 0x0C,
        INVALID_ATTRIBUTE_VALUE_LENGTH = 0x0D,
        UNLIKELY_ERROR = 0x0E,
        INSUFFICIENT_ENCRYPTION = 0x0F,
        UNSUPPORTED_GROUP_TYPE = 0x10,
        INSUFFICIENT_RESOURCES = 0x11
    }

    public enum PairingFailedCodes
    {
        SM_PAIRING_SUCCESS = 0x00,
        SM_PAIRING_TIMEOUT = 0x01,
        SM_PAIRING_FAILED = 0x02,
    }


    public enum WellKnownUUIDs
    {
        PRIMARY_SERVICE_UUID = 0x2800,
        SECONDARY_SERVICE_UUID = 0x2801,
        INCLUDE_SERVICE_UUID = 0x2802,
        CHARACTERISTIC_UUID = 0x2803,
        CHAR_EXTENDED_PROP_DESC_UUID = 0x2900,
        CHAR_USER_DESC_UUID = 0x2901,
        CHAR_CLIENT_CONFIG_DESC_UUID = 0x2902,
        CHAR_SERVER_CONFIG_DESC_UUID = 0x2903,
        CHAR_FORMAT_DESC_UUID = 0x2904,
        CHAR_AGGR_FMT_DESC_UUID = 0x2905,
        GATT_SERVICE_UUID = 0x1801,
        GAP_SERVICE_UUID = 0x1800,
        SERVICE_CHANGED_UUID = 0x2A05
    }

    public enum AccessPermissions
    {
        ATTR_NO_ACCESS = 0x00,
        ATTR_ACCESS_READ_ONLY = 0x01,
        ATTR_ACCESS_WRITE_REQ_ONLY = 0x02,
        ATTR_ACCESS_READ_WRITE = 0x03,
        ATTR_ACCESS_WRITE_WITHOUT_RESPONSE = 0x04,
        ATTR_ACCESS_SIGNED_WRITE_ALLOWED = 0x08,
        ATTR_ACCESS_WRITE_ANY = 0x0E //Allows all write procedures
    }

    public enum CharProperties
    {
        CHAR_PROP_BROADCAST = 0x01,
        CHAR_PROP_READ = 0x02,
        CHAR_PROP_WRITE_WITHOUT_RESP = 0x04,
        CHAR_PROP_WRITE = 0x08,
        CHAR_PROP_NOTIFY = 0x10,
        CHAR_PROP_INDICATE = 0x20,
        CHAR_PROP_SIGNED_WRITE = 0x40,
        CHAR_PROP_EXT = 0x80
    }

    public enum CharSecurityPermissions
    {
        ATTR_PERMISSION_NONE = 0x00,
        ATTR_PERMISSION_AUTHEN_READ = 0x01,
        ATTR_PERMISSION_AUTHOR_READ = 0x02,
        ATTR_PERMISSION_ENCRY_READ = 0x04,
        ATTR_PERMISSION_AUTHEN_WRITE = 0x08,
        ATTR_PERMISSION_AUTHOR_WRITE = 0x10,
        ATTR_PERMISSION_ENCRY_WRITE = 0x20
    }

    public enum UUIDTypes
    {
        UUID_TYPE_16 = 0x01,
        UUID_TYPE_128 = 0x02
    }

    public enum ServiceType
    {
        PRIMARY_SERVICE = 0x01,
        SECONDARY_SERVICE = 0x02
    }

    public enum CharLengthType
    {
        CHAR_VALUE_LEN_CONSTANT = 0x00,
        CHAR_VALUE_LEN_VARIABLE = 0x01
    }

    public enum EncryptionKeySize
    {
        MIN_ENCRY_KEY_SIZE = 0x07,
        MAX_ENCRY_KEY_SIZE = 0x10
    }

    public enum CharUnit
    {
        UNIT_UNITLESS = 0x2700,
        UNIT_TEMP_CELSIUS = 0x272F,
        UNIT_PRESSURE_BAR = 0x2780
    }



    public enum HCIErrorCodes
    {
        HCI_UNKNOWN_COMMAND = 0x01,
        HCI_NO_CONNECTION = 0x02,
        HCI_HARDWARE_FAILURE = 0x03,
        HCI_PAGE_TIMEOUT = 0x04,
        HCI_AUTHENTICATION_FAILURE = 0x05,
        HCI_PIN_OR_KEY_MISSING = 0x06,
        HCI_MEMORY_FULL = 0x07,
        HCI_CONNECTION_TIMEOUT = 0x08,
        HCI_MAX_NUMBER_OF_CONNECTIONS = 0x09,
        HCI_MAX_NUMBER_OF_SCO_CONNECTIONS = 0x0a,
        HCI_ACL_CONNECTION_EXISTS = 0x0b,
        HCI_COMMAND_DISALLOWED = 0x0c,
        HCI_REJECTED_LIMITED_RESOURCES = 0x0d,
        HCI_REJECTED_SECURITY = 0x0e,
        HCI_REJECTED_PERSONAL = 0x0f,
        HCI_HOST_TIMEOUT = 0x10,
        HCI_UNSUPPORTED_FEATURE = 0x11,
        HCI_INVALID_PARAMETERS = 0x12,
        HCI_OE_USER_ENDED_CONNECTION = 0x13,
        HCI_OE_LOW_RESOURCES = 0x14,
        HCI_OE_POWER_OFF = 0x15,
        HCI_CONNECTION_TERMINATED = 0x16,
        HCI_REPEATED_ATTEMPTS = 0x17,
        HCI_PAIRING_NOT_ALLOWED = 0x18,
        HCI_UNKNOWN_LMP_PDU = 0x19,
        HCI_UNSUPPORTED_REMOTE_FEATURE = 0x1a,
        HCI_SCO_OFFSET_REJECTED = 0x1b,
        HCI_SCO_INTERVAL_REJECTED = 0x1c,
        HCI_AIR_MODE_REJECTED = 0x1d,
        HCI_INVALID_LMP_PARAMETERS = 0x1e,
        HCI_UNSPECIFIED_ERROR = 0x1f,
        HCI_UNSUPPORTED_LMP_PARAMETER_VALUE = 0x20,
        HCI_ROLE_CHANGE_NOT_ALLOWED = 0x21,
        HCI_LMP_RESPONSE_TIMEOUT = 0x22,
        HCI_LMP_ERROR_TRANSACTION_COLLISION = 0x23,
        HCI_LMP_PDU_NOT_ALLOWED = 0x24,
        HCI_ENCRYPTION_MODE_NOT_ACCEPTED = 0x25,
        HCI_UNIT_LINK_KEY_USED = 0x26,
        HCI_QOS_NOT_SUPPORTED = 0x27,
        HCI_INSTANT_PASSED = 0x28,
        HCI_PAIRING_NOT_SUPPORTED = 0x29,
        HCI_TRANSACTION_COLLISION = 0x2a,
        HCI_QOS_UNACCEPTABLE_PARAMETER = 0x2c,
        HCI_QOS_REJECTED = 0x2d,
        HCI_CLASSIFICATION_NOT_SUPPORTED = 0x2e,
        HCI_INSUFFICIENT_SECURITY = 0x2f,
        HCI_PARAMETER_OUT_OF_RANGE = 0x30,
        HCI_ROLE_SWITCH_PENDING = 0x32,
        HCI_SLOT_VIOLATION = 0x34,
        HCI_ROLE_SWITCH_FAILED = 0x35,
        HCI_EIR_TOO_LARGE = 0x36,
        HCI_SIMPLE_PAIRING_NOT_SUPPORTED = 0x37,
        HCI_HOST_BUSY_PAIRING = 0x38,
        HCI_CONN_REJ_NO_CH_FOUND = 0x39,
        HCI_CONTROLLER_BUSY = 0x3A,
        HCI_UNACCEPTABLE_CONN_INTERV = 0x3B,
        HCI_DIRECTED_ADV_TIMEOUT = 0x3C,
        HCI_CONN_TERM_MIC_FAIL = 0x3D,
        HCI_CONN_FAIL_TO_BE_ESTABL = 0x3E,
        HCI_MAC_CONN_FAILED = 0x3F
    }


    //GAP LAYER


    public enum UUIDs_GAP
    {
        GAP_SERVICE_UUID = 0x1800,
        DEVICE_NAME_UUID = 0x2A00,
        APPEARANCE_UUID = 0x2A01,
        PERIPHERAL_PRIVACY_FLAG_UUID = 0x2A02,
        RECONNECTION_ADDR_UUID = 0x2A03,
        PERIPHERAL_PREFERRED_CONN_PARAMS_UUID = 0x2A04
    }

    public enum AdvBitType
    {
        FLAG_BIT_LE_LIMITED_DISCOVERABLE_MODE = 0x01,
        FLAG_BIT_LE_GENERAL_DISCOVERABLE_MODE = 0x02,
        FLAG_BIT_BR_EDR_NOT_SUPPORTED = 0x04,
        FLAG_BIT_LE_BR_EDR_CONTROLLER = 0x08,
        FLAG_BIT_LE_BR_EDR_HOST = 0x10
    }

    public enum UUIDAdvTypes
    {
        AD_TYPE_16_BIT_SERV_UUID = 0x02,
        AD_TYPE_16_BIT_SERV_UUID_CMPLT_LIST = 0x03,
        AD_TYPE_32_BIT_SERV_UUID = 0x04,
        AD_TYPE_32_BIT_SERV_UUID_CMPLT_LIST = 0x05,
        AD_TYPE_128_BIT_SERV_UUID = 0x06,
        AD_TYPE_128_BIT_SERV_UUID_CMPLT_LIST = 0x07
    }

    public enum AdvRange
    {
        DIR_CONN_ADV_INT_MIN = 0x190,  /*250ms*/
        DIR_CONN_ADV_INT_MAX = 0x320,  /*500ms*/
        UNDIR_CONN_ADV_INT_MIN = 0x800,  /*1.28s*/
        UNDIR_CONN_ADV_INT_MAX = 0x1000, /*2.56s*/
        LIM_DISC_ADV_INT_MIN = 0x190,  /*250ms*/
        LIM_DISC_ADV_INT_MAX = 0x320,  /*500ms*/
        GEN_DISC_ADV_INT_MIN = 0x800,  /*1.28s*/
        GEN_DISC_ADV_INT_MAX = 0x1000,  /*2.56s*/
    }

    public enum RoleForBlueNRG_MS
    {
        GAP_PERIPHERAL_ROLE = 0x01,
        GAP_BROADCASTER_ROLE = 0x02,
        GAP_CENTRAL_ROLE = 0x04,
        GAP_OBSERVER_ROLE = 0x08
    }

    public enum RoleForBlueNRG
    {
        GAP_PERIPHERAL_ROLE = 0x01,
        GAP_BROADCASTER_ROLE = 0x02,
        GAP_CENTRAL_ROLE = 0x03,//?? 0x04
        GAP_OBSERVER_ROLE = 0x04
    }

    public enum ModeGap
    {
        MODE1 = 1, //Mode 1: slave/master, 1 connection only, small GATT database (RAM2 off during sleep)
        MODE2 = 2, //Mode 2: slave/master, 1 connection only, large GATT database (RAM2 on during sleep)
        MODE3 = 3, //Mode 3: slave/master, 8 connections, small GATT database (RAM2 on during sleep)
        MODE4 = 4  //Mode 4: only on BlueNRG-MS FW stack version > 7.1a, slave/master, simultaneous advertising and scanning, 
        //        up to 4 connections, small GATT database (RAM2 on during sleep)
    }

    public enum ProcedureCodeForGAPProcedureCompleteEvent
    {
        GAP_LIMITED_DISCOVERY_PROC = 0x01,
        GAP_GENERAL_DISCOVERY_PROC = 0x02,
        GAP_NAME_DISCOVERY_PROC = 0x04,
        GAP_AUTO_CONNECTION_ESTABLISHMENT_PROC = 0x08,
        GAP_GENERAL_CONNECTION_ESTABLISHMENT_PROC = 0x10,
        GAP_SELECTIVE_CONNECTION_ESTABLISHMENT_PROC = 0x20,
        GAP_DIRECT_CONNECTION_ESTABLISHMENT_PROC = 0x40,
        GAP_OBSERVATION_PROC = 0x80
    }

    public enum BLEStatus
    {
        BLE_STATUS_SUCCESS = 0x00,
        ERR_UNKNOWN_HCI_COMMAND = 0x01,
        ERR_UNKNOWN_CONN_IDENTIFIER = 0x02,
        ERR_AUTH_FAILURE = 0x05,
        ERR_PIN_OR_KEY_MISSING = 0x06,
        ERR_MEM_CAPACITY_EXCEEDED = 0x07,
        ERR_CONNECTION_TIMEOUT = 0x08,
        ERR_COMMAND_DISALLOWED = 0x0C,
        ERR_UNSUPPORTED_FEATURE = 0x11,
        ERR_INVALID_HCI_CMD_PARAMS = 0x12,
        ERR_RMT_USR_TERM_CONN = 0x13,
        ERR_RMT_DEV_TERM_CONN_LOW_RESRCES = 0x14,
        ERR_RMT_DEV_TERM_CONN_POWER_OFF = 0x15,
        ERR_LOCAL_HOST_TERM_CONN = 0x16,
        ERR_UNSUPP_RMT_FEATURE = 0x1A,
        ERR_INVALID_LMP_PARAM = 0x1E,
        ERR_UNSPECIFIED_ERROR = 0x1F,
        ERR_LL_RESP_TIMEOUT = 0x22,
        ERR_LMP_PDU_NOT_ALLOWED = 0x24,
        ERR_INSTANT_PASSED = 0x28,
        ERR_PAIR_UNIT_KEY_NOT_SUPP = 0x29,
        ERR_CONTROLLER_BUSY = 0x3A,
        ERR_DIRECTED_ADV_TIMEOUT = 0x3C,
        ERR_CONN_END_WITH_MIC_FAILURE = 0x3D,
        ERR_CONN_FAILED_TO_ESTABLISH = 0x3E,
        BLE_STATUS_FAILED = 0x41,
        BLE_STATUS_INVALID_PARAMS = 0x42,
        BLE_STATUS_BUSY = 0x43,
        BLE_STATUS_INVALID_LEN_PDU = 0x44,
        BLE_STATUS_PENDING = 0x45,
        BLE_STATUS_NOT_ALLOWED = 0x46,
        BLE_STATUS_ERROR = 0x47,
        BLE_STATUS_ADDR_NOT_RESOLVED = 0x48,
        FLASH_READ_FAILED = 0x49,
        FLASH_WRITE_FAILED = 0x4A,
        FLASH_ERASE_FAILED = 0x4B,
        BLE_STATUS_INVALID_CID = 0x50,
        TIMER_NOT_VALID_LAYER = 0x54,
        TIMER_INSUFFICIENT_RESOURCES = 0x55,
        BLE_STATUS_CSRK_NOT_FOUND = 0x5A,
        BLE_STATUS_IRK_NOT_FOUND = 0x5B,
        BLE_STATUS_DEV_NOT_FOUND_IN_DB = 0x5C,
        BLE_STATUS_SEC_DB_FULL = 0x5D,
        BLE_STATUS_DEV_NOT_BONDED = 0x5E,
        BLE_STATUS_DEV_IN_BLACKLIST = 0x5F,
        BLE_STATUS_INVALID_HANDLE = 0x60,
        BLE_STATUS_INVALID_PARAMETER = 0x61,
        BLE_STATUS_OUT_OF_HANDLE = 0x62,
        BLE_STATUS_INVALID_OPERATION = 0x63,
        BLE_STATUS_INSUFFICIENT_RESOURCES = 0x64,
        BLE_INSUFFICIENT_ENC_KEYSIZE = 0x65,
        BLE_STATUS_CHARAC_ALREADY_EXISTS = 0x66,
        BLE_STATUS_TIMEOUT = 0xFF,
        BLE_STATUS_PROFILE_ALREADY_INITIALIZED = 0xF0,
        BLE_STATUS_NULL_PARAM = 0xF1
    }
}
