package org.apache.ratis.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestMD5Hash {

  @Test
  void constructsMD5HashWithCorrectLength() {
    MD5Hash hash = new MD5Hash();
    assertEquals(16, hash.getDigest().length);
  }

  @Test
  void constructsMD5HashFromHexString() {
    String hex = "0123456789abcdef0123456789abcdef";
    MD5Hash hash = new MD5Hash(hex);
    assertEquals(hex, hash.toString());
  }

  @Test
  void constructsMD5HashWithSpecifiedValue() {
    byte[] digest = new byte[16];
    MD5Hash hash = new MD5Hash(digest);
    assertArrayEquals(digest, hash.getDigest());
  }

  @Test
  void throwsExceptionForInvalidDigestLength() {
    byte[] digest = new byte[10];
    assertThrows(IllegalArgumentException.class, () -> new MD5Hash(digest));
  }

  @Test
  void readsAndWritesFieldsCorrectly() throws IOException {
    byte[] digest = new byte[16];
    MD5Hash hash = new MD5Hash(digest);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(out);
    hash.write(dataOut);

    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    DataInputStream dataIn = new DataInputStream(in);
    MD5Hash readHash = MD5Hash.read(dataIn);
    assertArrayEquals(digest, readHash.getDigest());
  }

  @Test
  void copiesContentsFromAnotherInstance() {
    byte[] digest = new byte[16];
    MD5Hash hash1 = new MD5Hash(digest);
    MD5Hash hash2 = new MD5Hash();
    hash2.set(hash1);
    assertArrayEquals(hash1.getDigest(), hash2.getDigest());
  }

  @Test
  void constructsHashValueForByteArray() {
    byte[] data = "test".getBytes();
    MD5Hash hash = MD5Hash.digest(data);
    assertNotNull(hash);
  }

  @Test
  void constructsHashValueForInputStream() throws IOException {
    byte[] data = "test".getBytes();
    InputStream in = new ByteArrayInputStream(data);
    MD5Hash hash = MD5Hash.digest(in);
    assertNotNull(hash);
  }

  @Test
  void constructsHashValueForArrayOfByteArray() {
    byte[][] dataArr = { "test1".getBytes(), "test2".getBytes() };
    MD5Hash hash = MD5Hash.digest(dataArr, 0, 5);
    assertNotNull(hash);
  }

  @Test
  void returnsHalfSizedVersionOfMD5() {
    byte[] digest = new byte[16];
    MD5Hash hash = new MD5Hash(digest);
    long halfDigest = hash.halfDigest();
    assertEquals(0, halfDigest);
  }

  @Test
  void returnsQuarterSizedVersionOfMD5() {
    byte[] digest = new byte[16];
    MD5Hash hash = new MD5Hash(digest);
    int quarterDigest = hash.quarterDigest();
    assertEquals(0, quarterDigest);
  }

  @Test
  void equalsReturnsTrueForEqualMD5Hashes() {
    byte[] digest = new byte[16];
    MD5Hash hash1 = new MD5Hash(digest);
    MD5Hash hash2 = new MD5Hash(digest);
    assertEquals(hash1, hash2);
  }

  @Test
  void equalsReturnsFalseForDifferentMD5Hashes() {
    byte[] digest1 = new byte[16];
    byte[] digest2 = new byte[16];
    digest2[0] = 1;
    MD5Hash hash1 = new MD5Hash(digest1);
    MD5Hash hash2 = new MD5Hash(digest2);
    assertNotEquals(hash1, hash2);
  }

  @Test
  void hashCodeReturnsCorrectValue() {
    byte[] digest = new byte[16];
    MD5Hash hash = new MD5Hash(digest);
    assertEquals(0, hash.hashCode());
  }

  @Test
  void toStringReturnsCorrectHexRepresentation() {
    byte[] digest = new byte[16];
    MD5Hash hash = new MD5Hash(digest);
    assertEquals("00000000000000000000000000000000", hash.toString());
  }

  @Test
  void setDigestFromHexString() {
    String hex = "0123456789abcdef0123456789abcdef";
    MD5Hash hash = new MD5Hash();
    hash.setDigest(hex);
    assertEquals(hex, hash.toString());
  }

  @Test
  void throwsExceptionForInvalidHexStringLength() {
    String hex = "0123456789abcdef";
    MD5Hash hash = new MD5Hash();
    assertThrows(IllegalArgumentException.class, () -> hash.setDigest(hex));
  }
}