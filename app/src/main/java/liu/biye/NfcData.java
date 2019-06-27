package liu.biye;

public class NfcData{
    private int sectorIndex;
    private int blockIndex;
    private   byte[] data;

    public NfcData(int sectorIndex, int blockIndex, byte[] data) {
        this.sectorIndex = sectorIndex;
        this.blockIndex = blockIndex;
        this.data = data;
    }

    public int getSectorIndex() {
        return sectorIndex;
    }

    public void setSectorIndex(int sectorIndex) {
        this.sectorIndex = sectorIndex;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}