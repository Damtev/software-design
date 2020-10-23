package ru.akirakozov.sd.refactoring.html;

public class HtmlResponseBuilder {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final StringBuilder builder = new StringBuilder();

    public void appendHeader(final String headerContent, final int headerLevel) {
        if (headerLevel < 1) {
            throw new IllegalArgumentException("Header level = " + headerLevel + " cannot be less than 1");
        }
        builder.append(format("<h%d>%s</h%d>%s", headerLevel, headerContent, headerLevel, LINE_SEPARATOR));

    }

    public void appendBreakLine() {
        builder.append(format("</br>%s", LINE_SEPARATOR));

    }

    public HtmlResponseBuilder append(final String... lines) {
        for (final String line : lines) {
            builder.append(line);
        }

        return this;
    }

    public HtmlResponseBuilder appendNextLine(final String... lines) {
        for (final String line : lines) {
            builder.append(line).append(LINE_SEPARATOR);
        }

        return this;
    }

    @Override
    public String toString() {
        return format("<html><body>%s%s</body></html>", LINE_SEPARATOR, builder.toString());
    }

    private String format(final String format, final Object... args) {
        return String.format(format, args);
    }
}
