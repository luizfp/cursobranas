package br.com.luizfp.cursobranas.domain.entity;

public record ShippedItem(double heightCm,
                          double widthCm,
                          double lengthCm,
                          double weightKg) {

    public double calculateVolumeInCubicMeters() {
        return cm3Tom3(heightCm * widthCm * lengthCm);
    }

    public double cm3Tom3(final double cubicCentimeters) {
        return cubicCentimeters / 1000000;
    }

    public double calculateDensity() {
        return weightKg / calculateVolumeInCubicMeters();
    }
}
