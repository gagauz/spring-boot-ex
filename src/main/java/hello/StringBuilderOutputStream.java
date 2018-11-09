package hello;
import java.io.IOException;
import java.io.OutputStream;

public class StringBuilderOutputStream extends OutputStream {

    private final StringBuilder stringBuilder;

    public StringBuilderOutputStream(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public StringBuilderOutputStream() {
        this(new StringBuilder());
    }

    @Override
    public void write(int b) throws IOException {
        stringBuilder.append((char) b);
    }

}
