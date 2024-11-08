package com.backendwave.services;

/**
 * Interface pour l'envoi d'emails
 */
public interface EmailService {

    /**
     * Envoie un email simple
     *
     * @param to Adresse email du destinataire
     * @param subject Sujet de l'email
     * @param text Contenu de l'email
     * @throws RuntimeException Si une erreur survient pendant l'envoi
     */
    void sendEmail(String to, String subject, String text);

    /**
     * Envoie un email avec une pièce jointe
     *
     * @param to Adresse email du destinataire
     * @param subject Sujet de l'email
     * @param text Contenu de l'email
     * @param attachment Contenu de la pièce jointe en bytes
     * @param filename Nom du fichier joint
     * @throws RuntimeException Si une erreur survient pendant l'envoi
     */
    void sendEmailWithAttachment(String to, String subject, String text, byte[] attachment, String filename);
}