package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

public class Cpf {
    private static final int CPF_VALID_LENGTH = 11;
    private static final int[] WEIGHTS_CPF_FIRST_VERIFIER_DIGIT = {10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHTS_CPF_SECOND_VERIFIER_DIGIT = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    @NotNull
    private final String cpf;

    public Cpf(@NotNull final String cpf) {
        this.cpf = cpf;
        if (!isValid()) {
            throw new InvalidCpfException();
        }
    }

    @NotNull
    public String getRawCpf() {
        return getOnlyNumbers();
    }

    private boolean isValid() {
        final String cleanedCpf = getOnlyNumbers();
        if (cleanedCpf.length() != CPF_VALID_LENGTH || areAllDigitsEqual(cleanedCpf)) {
            return false;
        }
        final int firstVerifierDigit = calcularDigito(cleanedCpf.substring(0, 9),
                                                      WEIGHTS_CPF_FIRST_VERIFIER_DIGIT);
        final int secondVerifierDigit = calcularDigito(cleanedCpf.substring(0, 9) + firstVerifierDigit,
                                                       WEIGHTS_CPF_SECOND_VERIFIER_DIGIT);
        final String generatedCpf = cleanedCpf.substring(0, 9) + firstVerifierDigit + secondVerifierDigit;
        return cleanedCpf.equals(generatedCpf);
    }

    @NotNull
    private String getOnlyNumbers() {
        return cpf.replaceAll("\\D+", "");
    }

    private static int calcularDigito(@NotNull final String cpfPart, final int[] weightsCpf) {
        int verifierDigitValue = 0;
        for (int i = 0; i < cpfPart.length(); i++) {
            int cpfPosition = Integer.parseInt(cpfPart.substring(i, i + 1));
            verifierDigitValue += cpfPosition * weightsCpf[i];
        }
        verifierDigitValue = 11 - verifierDigitValue % 11;
        return verifierDigitValue > 9 ? 0 : verifierDigitValue;
    }

    private boolean areAllDigitsEqual(@NotNull final String cleanedCpf) {
        return cleanedCpf
                .chars()
                .allMatch(c -> c == cleanedCpf.charAt(0));
    }
}
