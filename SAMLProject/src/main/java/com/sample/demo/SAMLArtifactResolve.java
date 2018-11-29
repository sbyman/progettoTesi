package com.sample.demo;

import java.util.Random;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Artifact;
import org.opensaml.saml2.core.ArtifactResolve;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;

public class SAMLArtifactResolve {

	private static XMLObjectBuilderFactory builderFactory;

	public static XMLObjectBuilderFactory getSAMLBuilder() throws ConfigurationException {

		if (builderFactory == null) {
			// OpenSAML 2.3
			DefaultBootstrap.bootstrap();
			builderFactory = Configuration.getBuilderFactory();
		}

		return builderFactory;
	}

	public String generateRandomHexString(int length) {
		Random random = new Random();

		StringBuilder sb = new StringBuilder();

		while (sb.length() < length) {
			sb.append(Integer.toHexString(random.nextInt()));
		}

		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	private ArtifactResolve buildArtifactResolve(SAMLInputContainer input, String SAMLart)
			throws ConfigurationException {
		// Crea l'oggetto issuer
		SAMLObjectBuilder issuerBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
		Issuer issuer = (Issuer) issuerBuilder.buildObject();
		issuer.setValue(input.getIssuer());

		// Crea l'oggetto artifact
		SAMLObjectBuilder artifactBuilder = (SAMLObjectBuilder) getSAMLBuilder()
				.getBuilder(Artifact.DEFAULT_ELEMENT_NAME);
		Artifact artifact = (Artifact) artifactBuilder.buildObject();
		artifact.setArtifact(SAMLart);

		// Crea l'oggetto artifact resolve
		SAMLObjectBuilder artifactResolveBuilder = (SAMLObjectBuilder) getSAMLBuilder()
				.getBuilder(ArtifactResolve.DEFAULT_ELEMENT_NAME);
		ArtifactResolve artifactResolve = (ArtifactResolve) artifactResolveBuilder
				.buildObject("urn:oasis:names:tc:SAML:2.0:protocol", "ArtifactResolve", "samlp");
		artifactResolve.setID("SP_TEST_" + generateRandomHexString(32));
		artifactResolve.setIssueInstant(DateTime.now());
		artifactResolve.setVersion(SAMLVersion.VERSION_20);
		artifactResolve.setDestination(input.getDestination());

		artifactResolve.setArtifact(artifact);
		artifactResolve.setIssuer(issuer);

		//Firma l'artifact request
		SAMLSignature samlSign = new SAMLSignature();
		samlSign.putSignature(artifactResolve);

		return artifactResolve;
	}

	public ArtifactResolve createArtifactResolve(SAMLInputContainer input, String SAMLart)
			throws ConfigurationException {
		return buildArtifactResolve(input, SAMLart);
	}
}
