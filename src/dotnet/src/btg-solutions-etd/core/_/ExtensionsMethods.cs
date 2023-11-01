namespace BtgPactualSolutions.Etd.core {
    using QuickFix;
    using System;
    public static class ExtensionsMethods {
        public static bool TryGetValue<T>(this FieldMap msg, int tag, out T value) {
            value = default;
            try {
                Type typeT = typeof(T);
                if (typeT.FullName == typeof(string).FullName) {
                    value = (T)Convert.ChangeType(msg.GetString(tag), typeT);
                    return true;
                }
                else if (typeT.FullName == typeof(int).FullName) {
                    value = (T)Convert.ChangeType(msg.GetInt(tag), typeT);
                    return true;
                } else if (typeT.FullName == typeof(long).FullName) {
                    value = (T)Convert.ChangeType(msg.GetInt(tag), typeT);
                    return true;
                } else if (typeT.FullName == typeof(decimal).FullName) {
                    value = (T)Convert.ChangeType(msg.GetDecimal(tag), typeT);
                    return true;
                }
                else if (typeT.FullName == typeof(double).FullName) {
                    value = (T)Convert.ChangeType(msg.GetDecimal(tag), typeT);
                    return true;
                }
                else if (typeT.FullName == typeof(char).FullName) {
                    value = (T)Convert.ChangeType(msg.GetChar(tag), typeT);
                    return true;
                }
                else if (typeT.FullName == typeof(bool).FullName) {
                    value = (T)Convert.ChangeType(msg.GetBoolean(tag), typeT);
                    return true;
                }
                else if (typeT.FullName == typeof(DateTime).FullName) {
                    value = (T)Convert.ChangeType(msg.GetDateTime(tag), typeT);
                    return true;
                }
                return false;
            }
            catch {
                return false;
            }
        }
    }
}
