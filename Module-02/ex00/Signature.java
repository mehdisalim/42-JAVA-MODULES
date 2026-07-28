public class Signature {
    final String extention;
    final byte[] signature;
    
    public Signature(String extention, byte[] signature) {
        this.extention = extention;
        this.signature = signature;
    }

    public String getExtention() {
        return extention;
    }

    public byte[] getSignature() {
        return signature;
    }

    
}
