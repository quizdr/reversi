/**
 * @file
 *
 * @brief Bit Works  module implementation.
 *
 * @par bit_works.c
 * <tt>
 * This file is part of the reversi program
 * http://github.com/rcrr/reversi
 * </tt>
 * @author Roberto Corradini mailto:rob_corradini@yahoo.it
 * @copyright 2013, 2014 Roberto Corradini. All rights reserved.
 *
 * @par License
 * <tt>
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 3, or (at your option) any
 * later version.
 * \n
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * \n
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 * or visit the site <http://www.gnu.org/licenses/>.
 * </tt>
 */

#include "bit_works.h"

static const uint64 m1  = 0x5555555555555555; //binary: 0101...
static const uint64 m2  = 0x3333333333333333; //binary: 00110011..
static const uint64 m4  = 0x0f0f0f0f0f0f0f0f; //binary:  4 zeros,  4 ones ...
static const uint64 m8  = 0x00ff00ff00ff00ff; //binary:  8 zeros,  8 ones ...
static const uint64 m16 = 0x0000ffff0000ffff; //binary: 16 zeros, 16 ones ...
static const uint64 m32 = 0x00000000ffffffff; //binary: 32 zeros, 32 ones
static const uint64 hff = 0xffffffffffffffff; //binary: all ones
static const uint64 h01 = 0x0101010101010101; //the sum of 256 to the power of 0,1,2,3...

/* Log 2 array */
static const uint8 log2_array[] = {
  0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
  5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
  6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
  6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
  7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
  7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
  7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
  7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
};

/* Array for de Bruijn multiplication. */
static const uint8 debruijn_64_index[] = {
  63,  0, 58,  1, 59, 47, 53,  2,
  60, 39, 48, 27, 54, 33, 42,  3,
  61, 51, 37, 40, 49, 18, 28, 20,
  55, 30, 34, 11, 43, 14, 22,  4,
  62, 57, 46, 52, 38, 26, 32, 41,
  50, 36, 17, 19, 29, 10, 13, 21,
  56, 45, 25, 31, 35, 16,  9, 12,
  44, 24, 15,  8, 23,  7,  6,  5
};

/* 64 bit value for the de Bruijn's "magical" constant. */
static const uint64 debruijn_64_magic_constant = 0x07EDD5E59A4E28C2ULL;

/* Right shift for the de Bruijn's algorithm. */
static const int debruijn_64_shift_value = 58;

/*
 * This is a naive implementation, shown for comparison,
 * and to help in understanding the better functions.
 * It uses 24 arithmetic operations (shift, add, and).
 *
static int popcount_1(uint64 x) {
    x = (x & m1 ) + ((x >>  1) & m1 ); //put count of each  2 bits into those  2 bits 
    x = (x & m2 ) + ((x >>  2) & m2 ); //put count of each  4 bits into those  4 bits 
    x = (x & m4 ) + ((x >>  4) & m4 ); //put count of each  8 bits into those  8 bits 
    x = (x & m8 ) + ((x >>  8) & m8 ); //put count of each 16 bits into those 16 bits 
    x = (x & m16) + ((x >> 16) & m16); //put count of each 32 bits into those 32 bits 
    x = (x & m32) + ((x >> 32) & m32); //put count of each 64 bits into those 64 bits 
    return x;
}
*/

/*
 * This uses fewer arithmetic operations than any other known  
 * implementation on machines with slow multiplication.
 * It uses 17 arithmetic operations.
 *
static int popcount_2(uint64 x) {
    x -= (x >> 1) & m1;             //put count of each 2 bits into those 2 bits
    x = (x & m2) + ((x >> 2) & m2); //put count of each 4 bits into those 4 bits 
    x = (x + (x >> 4)) & m4;        //put count of each 8 bits into those 8 bits 
    x += x >>  8;                   //put count of each 16 bits into their lowest 8 bits
    x += x >> 16;                   //put count of each 32 bits into their lowest 8 bits
    x += x >> 32;                   //put count of each 64 bits into their lowest 8 bits
    return x & 0x7f;
}
*/

/**
 * @brief Returns the count of the bit set to 1 in the `x` argument.
 *
 * This uses fewer arithmetic operations than any other known  
 * implementation on machines with fast multiplication.
 * It uses 12 arithmetic operations, one of which is a multiply.
 *
 * @param [in] x the set that has to be counted
 * @return       the count of bit set in the `x` parameter
 */
int
bit_works_popcount (uint64 x) {
    x -= (x >> 1) & m1;             //put count of each 2 bits into those 2 bits
    x = (x & m2) + ((x >> 2) & m2); //put count of each 4 bits into those 4 bits 
    x = (x + (x >> 4)) & m4;        //put count of each 8 bits into those 8 bits 
    return (x * h01) >> 56;         //returns left 8 bits of x + (x<<8) + (x<<16) + (x<<24) + ... 
}

/*
 * This is better when most bits in x are 0
 * It uses 3 arithmetic operations and one comparison/branch per "1" bit in x.
 *
static int popcount_4(uint64 x) {
    int count;
    for (count=0; x; count++)
        x &= x-1;
    return count;
}
*/

/**
 * @brief Returns an HiLo struct of two int collecting the octal representation of the position of
 * the most significant bit set in the `bit_sequence` parameter.
 *
 * The method does not verify that the `bit_sequence` parameter must be different from `0ULL`.
 * When this happens it returns a value equal to `{hi=0, lo=0}` that is the expected
 * value when the parameter is equal to `1ULL`.
 *
 * @param [out] result       a reference to the result struct
 * @param [in]  bit_sequence a long value different from 0L
 * @return                   the octal value of the position of the most significant bit set
 *                           collected in a two cells struct
 */
void
bit_works_bitscan_MS1B_to_base8 (HiLo *result, uint64 bit_sequence)
{
  uint32 tmp;
  uint8  hi;

  hi = 0;

  if ((bit_sequence & 0xFFFFFFFF00000000) != 0ULL) {
    tmp = (uint32) (bit_sequence >> 32);
    hi += 4;
  } else {
    tmp = (uint32) bit_sequence;
  }

  if ((tmp & 0xFFFF0000) != 0) { tmp >>= 16; hi += 2; }
  if ((tmp & 0x0000FF00) != 0) { tmp >>=  8; hi += 1; }
  
  result->hi = hi;
  result->lo = log2_array[tmp];
  return;
}

/**
 * @brief Returns a value computed shifting the `bit_sequence` parameter
 * to left by a signed amount given by the `shift` parameter.
 *
 * @param bit_sequence the value that will be shifted
 * @param shift        the number of position to shift
 * @return             the shifted value
 */
uint64
bit_works_signed_left_shift (uint64 bit_sequence, int shift) {
  return shift >= 0 ? bit_sequence << shift : bit_sequence >> -shift;
}

/**
 * @brief Returns an int value having all the bit set in `bit_sequence` turned to `0`
 * except the most significant one.
 *
 * When parameter `bit_sequence` is equal to `0` it returns `0`.
 *
 * @param bit_sequence the value analyzed
 * @return             an value having set the bit most significative found in bit_sequence
 */
uint32
bit_works_highest_bit_set_32 (uint32 bit_sequence) {
  if (bit_sequence == 0x00000000) { return 0x00000000; }
  uint32 result = 0x00000001;
  uint32 tmp = bit_sequence;
  if ((tmp & 0xFFFF0000) != 0x00000000) { tmp >>= 16; result = 0x00010000; }
  if  (tmp > 0x000000FF)                { tmp >>=  8; result <<= 8; }
  result <<= log2_array[tmp];
  return result;
}

/**
 * @brief Returns an int value having all the bit set in `bit_sequence` turned to `0`
 * except the most significant one.
 *
 * When parameter `bit_sequence` is equal to `0` it returns `0`.
 *
 * @param bit_sequence the value analyzed
 * @return             an value having set the bit most significative found in bit_sequence
 */
uint8
bit_works_highest_bit_set_8 (uint8 bit_sequence) {
  if (bit_sequence == 0x00) { return 0x00; }
  uint8 result = 0x01;
  result <<= log2_array[bit_sequence];
  return result;
}

/**
 * @brief The `bitsequence` parameter must have one or two bits set.
 * Returns a bit sequence having set the bits between the two, or zero
 * when only one bit is set.
 *
 * For example: `00100010` returns `00011100`.
 *
 * When the input data doesn't meet the requirements the result is unpredictable.
 *
 * @param [in] bit_sequence the value to be scanned
 * @return                  a bit sequence having the internal bits set
 */
uint8
bit_works_fill_in_between (uint8 bit_sequence) {
  return ((0x01 << bit_works_bitscanMS1B_8(bit_sequence)) - 0x01)
    & ((~bit_sequence & 0xFF) ^ (bit_sequence - 0x01));
}

/**
 * @brief Returns the index of the most significant bit set in the `bit_sequence` parameter.
 *
 * Parameter `bit_sequence` must be different from `0`.
 * If no bit set is found, meaning that `bit_sequence` is equal to `0`, `0` is
 * returned, that is clearly a wrong value.
 *
 * The proposed technique does three divide and conqueror steps, then makes a lookup in a table
 * hosting the log2 value for integers up to 255.
 *
 * So far it is the preferred choice for the reversi implementation.
 *
 * @param bit_sequence uint64 value that is scanned
 * @return             the index `(0..63)` of the most significant bit set
 */
uint8
bit_works_bitscanMS1B_64 (const uint64 bit_sequence) {
  uint64 tmp = bit_sequence;
  uint8 result = 0x00;
  if ((tmp & 0xFFFFFFFF00000000) != 0ULL) { tmp >>= 32; result  = 32; }
  if  (tmp > 0x000000000000FFFF)          { tmp >>= 16; result |= 16; }
  if  (tmp > 0x00000000000000FF)          { tmp >>=  8; result |=  8; }
  result |= log2_array[(int) tmp];
  return result;
}

/**
 * @brief Returns the index of the most significant bit set in the `bit_sequence` parameter.
 *
 * @param bit_sequence uint8 value that is scanned
 * @return             the index (0..7) of the most significant bit set 
 */
uint8
bit_works_bitscanMS1B_8 (const uint8 bit_sequence) {
  uint8 result = 0x00;
  result |= log2_array[(int) bit_sequence];
  return result;
}

/**
 * @brief Returns the index of the least significant bit set in the `bit_sequence` parameter
 * via de Bruijn's perfect hashing.
 *
 * Parameter `bit_sequence` must be different from {@code 0L}.
 * If no bit set is found, meaning that `bit_sequence` is equal to `0ULL`, `63` is
 * returned, that is clearly a wrong value.
 *
 * See: <a href="https://chessprogramming.wikispaces.com/Bitscan#DeBruijnMultiplation" target="_blank">
 *      de Bruijn multiplication</a>
 *
 * @param bit_sequence value that is scanned
 * @return             the index of the least significant bit set
 */
uint8
bit_works_bitscanLS1B_64 (const uint64 bit_sequence) {
  /** mask isolates the least significant one bit (LS1B). */
  const uint64 mask = bit_sequence & (-bit_sequence);
  return debruijn_64_index[(mask * debruijn_64_magic_constant) >> debruijn_64_shift_value];
}

/**
 * @brief Returns a bit sequence having one bit set, the lowest found
 * in the `bit_sequence` parameter.
 *
 * @param bit_sequence the input value
 * @return             the filtered sequence
 */
uint64
bit_works_lowest_bit_set_64 (const uint64 bit_sequence) {
  return (bit_sequence & (bit_sequence - 1)) ^ bit_sequence;
}

/**
 * @brief Returns a bit sequence having one bit set, the lowest found
 * in the `bit_sequence` parameter.
 *
 * @param bit_sequence the input value
 * @return             the filtered sequence
 */
uint32
bit_works_lowest_bit_set_32 (const uint32 bit_sequence) {
  return (bit_sequence & (bit_sequence - 1)) ^ bit_sequence;
}

/**
 * @brief Returns a bit sequence having one bit set, the lowest found
 * in the `bit_sequence` parameter.
 *
 * @param bit_sequence the input value
 * @return             the filtered sequence
 */
uint8
bit_works_lowest_bit_set_8 (const uint8 bit_sequence) {
  return (bit_sequence & (bit_sequence - 1)) ^ bit_sequence;
}
