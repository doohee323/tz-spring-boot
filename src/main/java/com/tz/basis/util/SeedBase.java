package com.tz.basis.util;

/**
 * <pre>
 * </pre>
 * 
 * @version 1.0
 */
public class SeedBase {
	public int SEED_BLOCK_SIZE = 0x10; // BYTE
	// private int SEED_BLOCK_LEN = 128; // BIT
	private static int NoRounds = 16;
	private int NoRoundKeys = NoRounds * 2;
	private static int nSeedVersion = 0x0008; // SYS 2.0
	public int EncRoundKey[] = new int[NoRoundKeys];
	public int DecRoundKey[] = new int[NoRoundKeys];
	private long A, B, C, D, L0, L1, R0, R1, T0, T1;
	private int K[] = new int[32];

	// ******************** Key Scheduling **********************/
	/* Constants for key schedule: KC0 = golden ratio; KCi = ROTL(KCi-1, 1) */
	private static long KC0 = 0x9e3779b9L;
	private static long KC1 = 0x3c6ef373L;
	private static long KC2 = 0x78dde6e6L;
	private static long KC3 = 0xf1bbcdccL;
	private static long KC4 = 0xe3779b99L;
	private static long KC5 = 0xc6ef3733L;
	private static long KC6 = 0x8dde6e67L;
	private static long KC7 = 0x1bbcdccfL;
	private static long KC8 = 0x3779b99eL;
	private static long KC9 = 0x6ef3733cL;
	private static long KC10 = 0xdde6e678L;
	private static long KC11 = 0xbbcdccf1L;
	private static long KC12 = 0x779b99e3L;
	private static long KC13 = 0xef3733c6L;
	private static long KC14 = 0xde6e678dL;
	private static long KC15 = 0xbcdccf1bL;

	private static long SS0[] = { 0x2989a1a8L, 0x05858184L, 0x16c6d2d4L, 0x13c3d3d0L, 0x14445054L, 0x1d0d111cL,
			0x2c8ca0acL, 0x25052124L, 0x1d4d515cL, 0x03434340L, 0x18081018L, 0x1e0e121cL, 0x11415150L, 0x3cccf0fcL,
			0x0acac2c8L, 0x23436360L, 0x28082028L, 0x04444044L, 0x20002020L, 0x1d8d919cL, 0x20c0e0e0L, 0x22c2e2e0L,
			0x08c8c0c8L, 0x17071314L, 0x2585a1a4L, 0x0f8f838cL, 0x03030300L, 0x3b4b7378L, 0x3b8bb3b8L, 0x13031310L,
			0x12c2d2d0L, 0x2ecee2ecL, 0x30407070L, 0x0c8c808cL, 0x3f0f333cL, 0x2888a0a8L, 0x32023230L, 0x1dcdd1dcL,
			0x36c6f2f4L, 0x34447074L, 0x2ccce0ecL, 0x15859194L, 0x0b0b0308L, 0x17475354L, 0x1c4c505cL, 0x1b4b5358L,
			0x3d8db1bcL, 0x01010100L, 0x24042024L, 0x1c0c101cL, 0x33437370L, 0x18889098L, 0x10001010L, 0x0cccc0ccL,
			0x32c2f2f0L, 0x19c9d1d8L, 0x2c0c202cL, 0x27c7e3e4L, 0x32427270L, 0x03838380L, 0x1b8b9398L, 0x11c1d1d0L,
			0x06868284L, 0x09c9c1c8L, 0x20406060L, 0x10405050L, 0x2383a3a0L, 0x2bcbe3e8L, 0x0d0d010cL, 0x3686b2b4L,
			0x1e8e929cL, 0x0f4f434cL, 0x3787b3b4L, 0x1a4a5258L, 0x06c6c2c4L, 0x38487078L, 0x2686a2a4L, 0x12021210L,
			0x2f8fa3acL, 0x15c5d1d4L, 0x21416160L, 0x03c3c3c0L, 0x3484b0b4L, 0x01414140L, 0x12425250L, 0x3d4d717cL,
			0x0d8d818cL, 0x08080008L, 0x1f0f131cL, 0x19899198L, 0x00000000L, 0x19091118L, 0x04040004L, 0x13435350L,
			0x37c7f3f4L, 0x21c1e1e0L, 0x3dcdf1fcL, 0x36467274L, 0x2f0f232cL, 0x27072324L, 0x3080b0b0L, 0x0b8b8388L,
			0x0e0e020cL, 0x2b8ba3a8L, 0x2282a2a0L, 0x2e4e626cL, 0x13839390L, 0x0d4d414cL, 0x29496168L, 0x3c4c707cL,
			0x09090108L, 0x0a0a0208L, 0x3f8fb3bcL, 0x2fcfe3ecL, 0x33c3f3f0L, 0x05c5c1c4L, 0x07878384L, 0x14041014L,
			0x3ecef2fcL, 0x24446064L, 0x1eced2dcL, 0x2e0e222cL, 0x0b4b4348L, 0x1a0a1218L, 0x06060204L, 0x21012120L,
			0x2b4b6368L, 0x26466264L, 0x02020200L, 0x35c5f1f4L, 0x12829290L, 0x0a8a8288L, 0x0c0c000cL, 0x3383b3b0L,
			0x3e4e727cL, 0x10c0d0d0L, 0x3a4a7278L, 0x07474344L, 0x16869294L, 0x25c5e1e4L, 0x26062224L, 0x00808080L,
			0x2d8da1acL, 0x1fcfd3dcL, 0x2181a1a0L, 0x30003030L, 0x37073334L, 0x2e8ea2acL, 0x36063234L, 0x15051114L,
			0x22022220L, 0x38083038L, 0x34c4f0f4L, 0x2787a3a4L, 0x05454144L, 0x0c4c404cL, 0x01818180L, 0x29c9e1e8L,
			0x04848084L, 0x17879394L, 0x35053134L, 0x0bcbc3c8L, 0x0ecec2ccL, 0x3c0c303cL, 0x31417170L, 0x11011110L,
			0x07c7c3c4L, 0x09898188L, 0x35457174L, 0x3bcbf3f8L, 0x1acad2d8L, 0x38c8f0f8L, 0x14849094L, 0x19495158L,
			0x02828280L, 0x04c4c0c4L, 0x3fcff3fcL, 0x09494148L, 0x39093138L, 0x27476364L, 0x00c0c0c0L, 0x0fcfc3ccL,
			0x17c7d3d4L, 0x3888b0b8L, 0x0f0f030cL, 0x0e8e828cL, 0x02424240L, 0x23032320L, 0x11819190L, 0x2c4c606cL,
			0x1bcbd3d8L, 0x2484a0a4L, 0x34043034L, 0x31c1f1f0L, 0x08484048L, 0x02c2c2c0L, 0x2f4f636cL, 0x3d0d313cL,
			0x2d0d212cL, 0x00404040L, 0x3e8eb2bcL, 0x3e0e323cL, 0x3c8cb0bcL, 0x01c1c1c0L, 0x2a8aa2a8L, 0x3a8ab2b8L,
			0x0e4e424cL, 0x15455154L, 0x3b0b3338L, 0x1cccd0dcL, 0x28486068L, 0x3f4f737cL, 0x1c8c909cL, 0x18c8d0d8L,
			0x0a4a4248L, 0x16465254L, 0x37477374L, 0x2080a0a0L, 0x2dcde1ecL, 0x06464244L, 0x3585b1b4L, 0x2b0b2328L,
			0x25456164L, 0x3acaf2f8L, 0x23c3e3e0L, 0x3989b1b8L, 0x3181b1b0L, 0x1f8f939cL, 0x1e4e525cL, 0x39c9f1f8L,
			0x26c6e2e4L, 0x3282b2b0L, 0x31013130L, 0x2acae2e8L, 0x2d4d616cL, 0x1f4f535cL, 0x24c4e0e4L, 0x30c0f0f0L,
			0x0dcdc1ccL, 0x08888088L, 0x16061214L, 0x3a0a3238L, 0x18485058L, 0x14c4d0d4L, 0x22426260L, 0x29092128L,
			0x07070304L, 0x33033330L, 0x28c8e0e8L, 0x1b0b1318L, 0x05050104L, 0x39497178L, 0x10809090L, 0x2a4a6268L,
			0x2a0a2228L, 0x1a8a9298L };

	private static long SS1[] = { 0x38380830L, 0xe828c8e0L, 0x2c2d0d21L, 0xa42686a2L, 0xcc0fcfc3L, 0xdc1eced2L,
			0xb03383b3L, 0xb83888b0L, 0xac2f8fa3L, 0x60204060L, 0x54154551L, 0xc407c7c3L, 0x44044440L, 0x6c2f4f63L,
			0x682b4b63L, 0x581b4b53L, 0xc003c3c3L, 0x60224262L, 0x30330333L, 0xb43585b1L, 0x28290921L, 0xa02080a0L,
			0xe022c2e2L, 0xa42787a3L, 0xd013c3d3L, 0x90118191L, 0x10110111L, 0x04060602L, 0x1c1c0c10L, 0xbc3c8cb0L,
			0x34360632L, 0x480b4b43L, 0xec2fcfe3L, 0x88088880L, 0x6c2c4c60L, 0xa82888a0L, 0x14170713L, 0xc404c4c0L,
			0x14160612L, 0xf434c4f0L, 0xc002c2c2L, 0x44054541L, 0xe021c1e1L, 0xd416c6d2L, 0x3c3f0f33L, 0x3c3d0d31L,
			0x8c0e8e82L, 0x98188890L, 0x28280820L, 0x4c0e4e42L, 0xf436c6f2L, 0x3c3e0e32L, 0xa42585a1L, 0xf839c9f1L,
			0x0c0d0d01L, 0xdc1fcfd3L, 0xd818c8d0L, 0x282b0b23L, 0x64264662L, 0x783a4a72L, 0x24270723L, 0x2c2f0f23L,
			0xf031c1f1L, 0x70324272L, 0x40024242L, 0xd414c4d0L, 0x40014141L, 0xc000c0c0L, 0x70334373L, 0x64274763L,
			0xac2c8ca0L, 0x880b8b83L, 0xf437c7f3L, 0xac2d8da1L, 0x80008080L, 0x1c1f0f13L, 0xc80acac2L, 0x2c2c0c20L,
			0xa82a8aa2L, 0x34340430L, 0xd012c2d2L, 0x080b0b03L, 0xec2ecee2L, 0xe829c9e1L, 0x5c1d4d51L, 0x94148490L,
			0x18180810L, 0xf838c8f0L, 0x54174753L, 0xac2e8ea2L, 0x08080800L, 0xc405c5c1L, 0x10130313L, 0xcc0dcdc1L,
			0x84068682L, 0xb83989b1L, 0xfc3fcff3L, 0x7c3d4d71L, 0xc001c1c1L, 0x30310131L, 0xf435c5f1L, 0x880a8a82L,
			0x682a4a62L, 0xb03181b1L, 0xd011c1d1L, 0x20200020L, 0xd417c7d3L, 0x00020202L, 0x20220222L, 0x04040400L,
			0x68284860L, 0x70314171L, 0x04070703L, 0xd81bcbd3L, 0x9c1d8d91L, 0x98198991L, 0x60214161L, 0xbc3e8eb2L,
			0xe426c6e2L, 0x58194951L, 0xdc1dcdd1L, 0x50114151L, 0x90108090L, 0xdc1cccd0L, 0x981a8a92L, 0xa02383a3L,
			0xa82b8ba3L, 0xd010c0d0L, 0x80018181L, 0x0c0f0f03L, 0x44074743L, 0x181a0a12L, 0xe023c3e3L, 0xec2ccce0L,
			0x8c0d8d81L, 0xbc3f8fb3L, 0x94168692L, 0x783b4b73L, 0x5c1c4c50L, 0xa02282a2L, 0xa02181a1L, 0x60234363L,
			0x20230323L, 0x4c0d4d41L, 0xc808c8c0L, 0x9c1e8e92L, 0x9c1c8c90L, 0x383a0a32L, 0x0c0c0c00L, 0x2c2e0e22L,
			0xb83a8ab2L, 0x6c2e4e62L, 0x9c1f8f93L, 0x581a4a52L, 0xf032c2f2L, 0x90128292L, 0xf033c3f3L, 0x48094941L,
			0x78384870L, 0xcc0cccc0L, 0x14150511L, 0xf83bcbf3L, 0x70304070L, 0x74354571L, 0x7c3f4f73L, 0x34350531L,
			0x10100010L, 0x00030303L, 0x64244460L, 0x6c2d4d61L, 0xc406c6c2L, 0x74344470L, 0xd415c5d1L, 0xb43484b0L,
			0xe82acae2L, 0x08090901L, 0x74364672L, 0x18190911L, 0xfc3ecef2L, 0x40004040L, 0x10120212L, 0xe020c0e0L,
			0xbc3d8db1L, 0x04050501L, 0xf83acaf2L, 0x00010101L, 0xf030c0f0L, 0x282a0a22L, 0x5c1e4e52L, 0xa82989a1L,
			0x54164652L, 0x40034343L, 0x84058581L, 0x14140410L, 0x88098981L, 0x981b8b93L, 0xb03080b0L, 0xe425c5e1L,
			0x48084840L, 0x78394971L, 0x94178793L, 0xfc3cccf0L, 0x1c1e0e12L, 0x80028282L, 0x20210121L, 0x8c0c8c80L,
			0x181b0b13L, 0x5c1f4f53L, 0x74374773L, 0x54144450L, 0xb03282b2L, 0x1c1d0d11L, 0x24250521L, 0x4c0f4f43L,
			0x00000000L, 0x44064642L, 0xec2dcde1L, 0x58184850L, 0x50124252L, 0xe82bcbe3L, 0x7c3e4e72L, 0xd81acad2L,
			0xc809c9c1L, 0xfc3dcdf1L, 0x30300030L, 0x94158591L, 0x64254561L, 0x3c3c0c30L, 0xb43686b2L, 0xe424c4e0L,
			0xb83b8bb3L, 0x7c3c4c70L, 0x0c0e0e02L, 0x50104050L, 0x38390931L, 0x24260622L, 0x30320232L, 0x84048480L,
			0x68294961L, 0x90138393L, 0x34370733L, 0xe427c7e3L, 0x24240420L, 0xa42484a0L, 0xc80bcbc3L, 0x50134353L,
			0x080a0a02L, 0x84078783L, 0xd819c9d1L, 0x4c0c4c40L, 0x80038383L, 0x8c0f8f83L, 0xcc0ecec2L, 0x383b0b33L,
			0x480a4a42L, 0xb43787b3L };

	private static long SS2[] = { 0xa1a82989L, 0x81840585L, 0xd2d416c6L, 0xd3d013c3L, 0x50541444L, 0x111c1d0dL,
			0xa0ac2c8cL, 0x21242505L, 0x515c1d4dL, 0x43400343L, 0x10181808L, 0x121c1e0eL, 0x51501141L, 0xf0fc3cccL,
			0xc2c80acaL, 0x63602343L, 0x20282808L, 0x40440444L, 0x20202000L, 0x919c1d8dL, 0xe0e020c0L, 0xe2e022c2L,
			0xc0c808c8L, 0x13141707L, 0xa1a42585L, 0x838c0f8fL, 0x03000303L, 0x73783b4bL, 0xb3b83b8bL, 0x13101303L,
			0xd2d012c2L, 0xe2ec2eceL, 0x70703040L, 0x808c0c8cL, 0x333c3f0fL, 0xa0a82888L, 0x32303202L, 0xd1dc1dcdL,
			0xf2f436c6L, 0x70743444L, 0xe0ec2cccL, 0x91941585L, 0x03080b0bL, 0x53541747L, 0x505c1c4cL, 0x53581b4bL,
			0xb1bc3d8dL, 0x01000101L, 0x20242404L, 0x101c1c0cL, 0x73703343L, 0x90981888L, 0x10101000L, 0xc0cc0cccL,
			0xf2f032c2L, 0xd1d819c9L, 0x202c2c0cL, 0xe3e427c7L, 0x72703242L, 0x83800383L, 0x93981b8bL, 0xd1d011c1L,
			0x82840686L, 0xc1c809c9L, 0x60602040L, 0x50501040L, 0xa3a02383L, 0xe3e82bcbL, 0x010c0d0dL, 0xb2b43686L,
			0x929c1e8eL, 0x434c0f4fL, 0xb3b43787L, 0x52581a4aL, 0xc2c406c6L, 0x70783848L, 0xa2a42686L, 0x12101202L,
			0xa3ac2f8fL, 0xd1d415c5L, 0x61602141L, 0xc3c003c3L, 0xb0b43484L, 0x41400141L, 0x52501242L, 0x717c3d4dL,
			0x818c0d8dL, 0x00080808L, 0x131c1f0fL, 0x91981989L, 0x00000000L, 0x11181909L, 0x00040404L, 0x53501343L,
			0xf3f437c7L, 0xe1e021c1L, 0xf1fc3dcdL, 0x72743646L, 0x232c2f0fL, 0x23242707L, 0xb0b03080L, 0x83880b8bL,
			0x020c0e0eL, 0xa3a82b8bL, 0xa2a02282L, 0x626c2e4eL, 0x93901383L, 0x414c0d4dL, 0x61682949L, 0x707c3c4cL,
			0x01080909L, 0x02080a0aL, 0xb3bc3f8fL, 0xe3ec2fcfL, 0xf3f033c3L, 0xc1c405c5L, 0x83840787L, 0x10141404L,
			0xf2fc3eceL, 0x60642444L, 0xd2dc1eceL, 0x222c2e0eL, 0x43480b4bL, 0x12181a0aL, 0x02040606L, 0x21202101L,
			0x63682b4bL, 0x62642646L, 0x02000202L, 0xf1f435c5L, 0x92901282L, 0x82880a8aL, 0x000c0c0cL, 0xb3b03383L,
			0x727c3e4eL, 0xd0d010c0L, 0x72783a4aL, 0x43440747L, 0x92941686L, 0xe1e425c5L, 0x22242606L, 0x80800080L,
			0xa1ac2d8dL, 0xd3dc1fcfL, 0xa1a02181L, 0x30303000L, 0x33343707L, 0xa2ac2e8eL, 0x32343606L, 0x11141505L,
			0x22202202L, 0x30383808L, 0xf0f434c4L, 0xa3a42787L, 0x41440545L, 0x404c0c4cL, 0x81800181L, 0xe1e829c9L,
			0x80840484L, 0x93941787L, 0x31343505L, 0xc3c80bcbL, 0xc2cc0eceL, 0x303c3c0cL, 0x71703141L, 0x11101101L,
			0xc3c407c7L, 0x81880989L, 0x71743545L, 0xf3f83bcbL, 0xd2d81acaL, 0xf0f838c8L, 0x90941484L, 0x51581949L,
			0x82800282L, 0xc0c404c4L, 0xf3fc3fcfL, 0x41480949L, 0x31383909L, 0x63642747L, 0xc0c000c0L, 0xc3cc0fcfL,
			0xd3d417c7L, 0xb0b83888L, 0x030c0f0fL, 0x828c0e8eL, 0x42400242L, 0x23202303L, 0x91901181L, 0x606c2c4cL,
			0xd3d81bcbL, 0xa0a42484L, 0x30343404L, 0xf1f031c1L, 0x40480848L, 0xc2c002c2L, 0x636c2f4fL, 0x313c3d0dL,
			0x212c2d0dL, 0x40400040L, 0xb2bc3e8eL, 0x323c3e0eL, 0xb0bc3c8cL, 0xc1c001c1L, 0xa2a82a8aL, 0xb2b83a8aL,
			0x424c0e4eL, 0x51541545L, 0x33383b0bL, 0xd0dc1cccL, 0x60682848L, 0x737c3f4fL, 0x909c1c8cL, 0xd0d818c8L,
			0x42480a4aL, 0x52541646L, 0x73743747L, 0xa0a02080L, 0xe1ec2dcdL, 0x42440646L, 0xb1b43585L, 0x23282b0bL,
			0x61642545L, 0xf2f83acaL, 0xe3e023c3L, 0xb1b83989L, 0xb1b03181L, 0x939c1f8fL, 0x525c1e4eL, 0xf1f839c9L,
			0xe2e426c6L, 0xb2b03282L, 0x31303101L, 0xe2e82acaL, 0x616c2d4dL, 0x535c1f4fL, 0xe0e424c4L, 0xf0f030c0L,
			0xc1cc0dcdL, 0x80880888L, 0x12141606L, 0x32383a0aL, 0x50581848L, 0xd0d414c4L, 0x62602242L, 0x21282909L,
			0x03040707L, 0x33303303L, 0xe0e828c8L, 0x13181b0bL, 0x01040505L, 0x71783949L, 0x90901080L, 0x62682a4aL,
			0x22282a0aL, 0x92981a8aL };

	private static long SS3[] = { 0x08303838L, 0xc8e0e828L, 0x0d212c2dL, 0x86a2a426L, 0xcfc3cc0fL, 0xced2dc1eL,
			0x83b3b033L, 0x88b0b838L, 0x8fa3ac2fL, 0x40606020L, 0x45515415L, 0xc7c3c407L, 0x44404404L, 0x4f636c2fL,
			0x4b63682bL, 0x4b53581bL, 0xc3c3c003L, 0x42626022L, 0x03333033L, 0x85b1b435L, 0x09212829L, 0x80a0a020L,
			0xc2e2e022L, 0x87a3a427L, 0xc3d3d013L, 0x81919011L, 0x01111011L, 0x06020406L, 0x0c101c1cL, 0x8cb0bc3cL,
			0x06323436L, 0x4b43480bL, 0xcfe3ec2fL, 0x88808808L, 0x4c606c2cL, 0x88a0a828L, 0x07131417L, 0xc4c0c404L,
			0x06121416L, 0xc4f0f434L, 0xc2c2c002L, 0x45414405L, 0xc1e1e021L, 0xc6d2d416L, 0x0f333c3fL, 0x0d313c3dL,
			0x8e828c0eL, 0x88909818L, 0x08202828L, 0x4e424c0eL, 0xc6f2f436L, 0x0e323c3eL, 0x85a1a425L, 0xc9f1f839L,
			0x0d010c0dL, 0xcfd3dc1fL, 0xc8d0d818L, 0x0b23282bL, 0x46626426L, 0x4a72783aL, 0x07232427L, 0x0f232c2fL,
			0xc1f1f031L, 0x42727032L, 0x42424002L, 0xc4d0d414L, 0x41414001L, 0xc0c0c000L, 0x43737033L, 0x47636427L,
			0x8ca0ac2cL, 0x8b83880bL, 0xc7f3f437L, 0x8da1ac2dL, 0x80808000L, 0x0f131c1fL, 0xcac2c80aL, 0x0c202c2cL,
			0x8aa2a82aL, 0x04303434L, 0xc2d2d012L, 0x0b03080bL, 0xcee2ec2eL, 0xc9e1e829L, 0x4d515c1dL, 0x84909414L,
			0x08101818L, 0xc8f0f838L, 0x47535417L, 0x8ea2ac2eL, 0x08000808L, 0xc5c1c405L, 0x03131013L, 0xcdc1cc0dL,
			0x86828406L, 0x89b1b839L, 0xcff3fc3fL, 0x4d717c3dL, 0xc1c1c001L, 0x01313031L, 0xc5f1f435L, 0x8a82880aL,
			0x4a62682aL, 0x81b1b031L, 0xc1d1d011L, 0x00202020L, 0xc7d3d417L, 0x02020002L, 0x02222022L, 0x04000404L,
			0x48606828L, 0x41717031L, 0x07030407L, 0xcbd3d81bL, 0x8d919c1dL, 0x89919819L, 0x41616021L, 0x8eb2bc3eL,
			0xc6e2e426L, 0x49515819L, 0xcdd1dc1dL, 0x41515011L, 0x80909010L, 0xccd0dc1cL, 0x8a92981aL, 0x83a3a023L,
			0x8ba3a82bL, 0xc0d0d010L, 0x81818001L, 0x0f030c0fL, 0x47434407L, 0x0a12181aL, 0xc3e3e023L, 0xcce0ec2cL,
			0x8d818c0dL, 0x8fb3bc3fL, 0x86929416L, 0x4b73783bL, 0x4c505c1cL, 0x82a2a022L, 0x81a1a021L, 0x43636023L,
			0x03232023L, 0x4d414c0dL, 0xc8c0c808L, 0x8e929c1eL, 0x8c909c1cL, 0x0a32383aL, 0x0c000c0cL, 0x0e222c2eL,
			0x8ab2b83aL, 0x4e626c2eL, 0x8f939c1fL, 0x4a52581aL, 0xc2f2f032L, 0x82929012L, 0xc3f3f033L, 0x49414809L,
			0x48707838L, 0xccc0cc0cL, 0x05111415L, 0xcbf3f83bL, 0x40707030L, 0x45717435L, 0x4f737c3fL, 0x05313435L,
			0x00101010L, 0x03030003L, 0x44606424L, 0x4d616c2dL, 0xc6c2c406L, 0x44707434L, 0xc5d1d415L, 0x84b0b434L,
			0xcae2e82aL, 0x09010809L, 0x46727436L, 0x09111819L, 0xcef2fc3eL, 0x40404000L, 0x02121012L, 0xc0e0e020L,
			0x8db1bc3dL, 0x05010405L, 0xcaf2f83aL, 0x01010001L, 0xc0f0f030L, 0x0a22282aL, 0x4e525c1eL, 0x89a1a829L,
			0x46525416L, 0x43434003L, 0x85818405L, 0x04101414L, 0x89818809L, 0x8b93981bL, 0x80b0b030L, 0xc5e1e425L,
			0x48404808L, 0x49717839L, 0x87939417L, 0xccf0fc3cL, 0x0e121c1eL, 0x82828002L, 0x01212021L, 0x8c808c0cL,
			0x0b13181bL, 0x4f535c1fL, 0x47737437L, 0x44505414L, 0x82b2b032L, 0x0d111c1dL, 0x05212425L, 0x4f434c0fL,
			0x00000000L, 0x46424406L, 0xcde1ec2dL, 0x48505818L, 0x42525012L, 0xcbe3e82bL, 0x4e727c3eL, 0xcad2d81aL,
			0xc9c1c809L, 0xcdf1fc3dL, 0x00303030L, 0x85919415L, 0x45616425L, 0x0c303c3cL, 0x86b2b436L, 0xc4e0e424L,
			0x8bb3b83bL, 0x4c707c3cL, 0x0e020c0eL, 0x40505010L, 0x09313839L, 0x06222426L, 0x02323032L, 0x84808404L,
			0x49616829L, 0x83939013L, 0x07333437L, 0xc7e3e427L, 0x04202424L, 0x84a0a424L, 0xcbc3c80bL, 0x43535013L,
			0x0a02080aL, 0x87838407L, 0xc9d1d819L, 0x4c404c0cL, 0x83838003L, 0x8f838c0fL, 0xcec2cc0eL, 0x0b33383bL,
			0x4a42480aL, 0x87b3b437L };

	/**
	 * (val << x) 값과 (val >>> (32 - x)) 한 값을 BIT-OR 연산
	 *
	 * @param val
	 *          암호화시킬 데이타
	 * @param ㅌ
	 *          암호화시킬길이
	 *
	 * @return int
	 */
	private int LROTL(int val, int x) {
		return ((val << x) | (val >>> (32 - x)));
	}
	/*--------------------------------------------------------------------------
	* Function Name : LROTR()
	*--------------------------------------------------------------------------
	* Description : (val >>> x) 값과 (val << (32 - x)) 한 값을 BIT-OR 연산
	* Parameter : (int) val, (int) x
	* Return Value : int
	*--------------------------------------------------------------------------
	*/
	// private int LROTR(int val, int x)
	// {
	// return((val >>> x) | (val << (32 - x)));
	// }

	/**
	 * 메모리 방식이 Big-Endian 인 경우 Change
	 */
	private long EndianChange(long dws) {
		return (long) LROTL((int) dws, 8) & (long) 0x00ff00ff | LROTL((int) dws, 24) & (long) 0xff00ff00;
	}

	/**
	 * Seed Version을 nVersion 값으로 Setting
	 */
	public void SetSeedVersion(int nVersion) {
		nSeedVersion = nVersion;
	}

	/**
	 * 현재 Setting된 Seed Version을 가져온다
	 */
	public byte GetSeedVersion() {
		if (nSeedVersion == 9) {
			return 0x11;
		} else {
			return 0x20;
		}
	}

	/**
	 * 값의 2의 보수(byte) 를 unsigned값으로 변환하여 int로 return
	 *
	 * @param A
	 *          값의 2의 보수
	 */
	public int ChangeUnsigned(byte A) {
		int i;
		String temp, temp1;
		i = 0;

		if (A >= 0 && A <= 0x7F) {
			return A;
		}

		temp = Integer.toBinaryString(A);

		temp1 = temp.substring(24, 25);
		i += Integer.parseInt(temp1) * 128;

		temp1 = temp.substring(25, 26);
		i += Integer.parseInt(temp1) * 64;

		temp1 = temp.substring(26, 27);
		i += Integer.parseInt(temp1) * 32;

		temp1 = temp.substring(27, 28);
		i += Integer.parseInt(temp1) * 16;

		temp1 = temp.substring(28, 29);
		i += Integer.parseInt(temp1) * 8;

		temp1 = temp.substring(29, 30);
		i += Integer.parseInt(temp1) * 4;

		temp1 = temp.substring(30, 31);
		i += Integer.parseInt(temp1) * 2;

		temp1 = temp.substring(31, 32);
		i += Integer.parseInt(temp1);

		return i;
	}

	/**
	 * 값의 2의 보수(int)를 unsigned 값으로 변환하여 long으로 return
	 *
	 * @param a
	 *          값의 2의 보수
	 * @param long
	 *          unsigned 된 int값을 long으로 return
	 */
	private long ChangeUnsignedInt(int a) {
		String temp;
		long l = 0;

		temp = Integer.toHexString(a);
		l = Long.parseLong(temp, 16);
		return l;
	}

	/**
	 * long값의 하위 1 byte값을 int로 return
	 */
	private int GetB0(long A) {
		int ret;
		byte ret_byte;

		ret_byte = (byte) A;
		if (ret_byte < 0) {
			ret = ChangeUnsigned(ret_byte);
			return ret;
		} else {
			return ret_byte;
		}
	}

	/**
	 * long값 A를 >>> 8 한 값을 int로 return
	 */
	private int GetB1(long A) {
		int ret;
		byte ret_byte;

		ret_byte = (byte) (A >>> 8);
		if (ret_byte < 0) {
			ret = ChangeUnsigned(ret_byte);
			return ret;
		} else {
			return ret_byte;
		}

	}

	/**
	 * long값 A를 >>> 16 한 값을 int로 return
	 */
	private int GetB2(long A) {
		int ret;
		byte ret_byte;

		ret_byte = (byte) (A >>> 16);

		if (ret_byte < 0) {
			ret = ChangeUnsigned(ret_byte);
			return ret;
		} else {
			return ret_byte;
		}
	}

	/**
	 * long값 A를 >>> 24 한 값을 int로 return
	 */
	private int GetB3(long A) {
		int ret;
		byte ret_byte;

		ret_byte = (byte) (A >>> 24);

		if (ret_byte < 0) {
			ret = ChangeUnsigned(ret_byte);
			return ret;
		} else {
			return ret_byte;
		}

	}

	/**
	 * L0, L1값을 변환
	 */
	private void SeedRoundL(long a, long b, long c, long d, int point) {
		L0 = (int) a;
		L1 = (int) b;
		R0 = (int) c;
		R1 = (int) d;

		T0 = (int) (R0 ^ K[point]);
		T1 = (int) (R1 ^ K[point + 1]);

		T1 ^= (int) T0;
		T1 = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);

		T0 += (int) T1;
		T0 = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);

		T1 += (int) T0;
		T1 = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);

		T0 += (int) T1;
		L0 ^= (int) T0;
		L1 ^= (int) T1;
	}

	/**
	 * R0, R1값을 변환
	 */
	private void SeedRoundR(long a, long b, long c, long d, int point) {
		R0 = (int) a;
		R1 = (int) b;
		L0 = (int) c;
		L1 = (int) d;

		T0 = (int) (L0 ^ K[point]);
		T1 = (int) (L1 ^ K[point + 1]);

		T1 ^= (int) T0;
		T1 = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);

		T0 += (int) T1;
		T0 = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);

		T1 += (int) T0;
		T1 = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);

		T0 += (int) T1;
		R0 ^= (int) T0;
		R1 ^= (int) T1;
	}

	/**
	 * L0, L1값을 변환
	 */
	private void SeedRound1L(long a, long b, long c, long d, int point) {
		// 멤버변수에 값 할당
		L0 = (int) a;
		L1 = (int) b;
		R0 = (int) c;
		R1 = (int) d;

		T0 = (int) (R0 ^ K[point]);
		T1 = (int) (R1 ^ K[point + 1]);

		T0 ^= (int) T1;
		T0 = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);

		T1 += (int) T0;
		T1 = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);

		T0 += (int) T1;
		T0 = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);

		T1 += (int) T0;
		L0 ^= (int) T0;
		L1 ^= (int) T1;
	}

	/**
	 * R0, R1값을 변환
	 */
	private void SeedRound1R(long a, long b, long c, long d, int point) {
		// 멤버변수에 값 할당
		R0 = (int) a;
		R1 = (int) b;
		L0 = (int) c;
		L1 = (int) d;

		T0 = (int) (L0 ^ K[point]);
		T1 = (int) (L1 ^ K[point + 1]);

		T0 ^= (int) T1;
		T0 = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);

		T1 += (int) T0;
		T1 = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);

		T0 += (int) T1;
		T0 = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);

		T1 += (int) T0;
		R0 ^= (int) T0;
		R1 ^= (int) T1;

	}

	/**
	 * 주어진 키값을 Round
	 */
	public void SeedEncRoundKey(byte[] pbUserKey) {
		// int i;

		A = BytesToLong(pbUserKey, 0);
		B = BytesToLong(pbUserKey, 4);
		C = BytesToLong(pbUserKey, 8);
		D = BytesToLong(pbUserKey, 12);

		if (nSeedVersion == 9) {
			// BIG_ENDIAN
			A = EndianChange(A);
			B = EndianChange(B);
			C = EndianChange(C);
			D = EndianChange(D);
		} else {
		}

		T0 = A + C - KC0;
		T1 = B - D + KC0;

		EncRoundKey[0] = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);
		EncRoundKey[1] = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);

		EncRoundKeyUpdate0(2, A, B, C, D, KC1);
		EncRoundKeyUpdate1(4, A, B, C, D, KC2);
		EncRoundKeyUpdate0(6, A, B, C, D, KC3);
		EncRoundKeyUpdate1(8, A, B, C, D, KC4);
		EncRoundKeyUpdate0(10, A, B, C, D, KC5);
		EncRoundKeyUpdate1(12, A, B, C, D, KC6);
		EncRoundKeyUpdate0(14, A, B, C, D, KC7);
		EncRoundKeyUpdate1(16, A, B, C, D, KC8);
		EncRoundKeyUpdate0(18, A, B, C, D, KC9);
		EncRoundKeyUpdate1(20, A, B, C, D, KC10);
		EncRoundKeyUpdate0(22, A, B, C, D, KC11);
		if (NoRounds == 16) {
			EncRoundKeyUpdate1(24, A, B, C, D, KC12);
			EncRoundKeyUpdate0(26, A, B, C, D, KC13);
			EncRoundKeyUpdate1(28, A, B, C, D, KC14);
			EncRoundKeyUpdate0(30, A, B, C, D, KC15);
		}

		// return K;

	}

	/**
	 * 연산을 통해 얻어진 값을 EncRoundKey[] 의 point, point+1에 저장
	 */
	private void EncRoundKeyUpdate0(int point, long a, long b, long c, long d, long KC) {
		A = a;
		B = b;
		C = c;
		D = d;

		T0 = A;

		A = (ChangeUnsignedInt((int) A >>> 8) ^ (B << 24));
		B = (ChangeUnsignedInt((int) B >>> 8) ^ (T0 << 24));

		T0 = A + C - KC;
		T1 = B + KC - D;

		EncRoundKey[point] = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);
		EncRoundKey[point + 1] = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);
	}

	/**
	 * 연산을 통해 얻어진 값을 EncRoundKey[] 의 point, point+1에 저장
	 */
	private void EncRoundKeyUpdate1(int point, long a, long b, long c, long d, long KC) {
		A = a;
		B = b;
		C = c;
		D = d;
		int itemp = 0;
		byte btemp = 0;

		T0 = C;

		btemp = (byte) (D >>> 24);
		itemp = ChangeUnsigned(btemp);
		C = ((C << 8) ^ itemp);

		btemp = (byte) (T0 >>> 24);
		itemp = ChangeUnsigned(btemp);
		D = ((D << 8) ^ itemp);
		T0 = A + C - KC;
		T1 = B + KC - D;

		EncRoundKey[point] = (int) (SS0[GetB0(T0)] ^ SS1[GetB1(T0)] ^ SS2[GetB2(T0)] ^ SS3[GetB3(T0)]);
		EncRoundKey[point + 1] = (int) (SS0[GetB0(T1)] ^ SS1[GetB1(T1)] ^ SS2[GetB2(T1)] ^ SS3[GetB3(T1)]);
	}

	/**
	 * byte[] 데이타를 Long 로 변환해주는 함수
	 */
	private long BytesToLong(byte[] pbData, int start) {
		long ret;
		int temp;
		ret = 0;

		temp = pbData[start];
		if (temp < 0) {
			temp = ChangeUnsigned(pbData[start]);
		}
		ret ^= temp << 24;

		temp = pbData[start + 1];
		if (temp < 0) {
			temp = ChangeUnsigned(pbData[start + 1]);
		}
		ret ^= temp << 16;

		temp = pbData[start + 2];
		if (temp < 0) {
			temp = ChangeUnsigned(pbData[start + 2]);
		}
		ret ^= temp << 8;

		temp = pbData[start + 3];
		if (temp < 0) {
			temp = ChangeUnsigned(pbData[start + 3]);
		}
		ret ^= temp;

		return ret;
	}

	/**
	 * Long 데이타를 byte[] 로 변환해주는 함수
	 */
	private byte[] LongToBytes(long a) {
		byte[] ret = new byte[4];
		ret[0] = (byte) (a >>> 24);
		ret[1] = (byte) ((a >>> 16) & 0x00FF);
		ret[2] = (byte) ((a >>> 8) & 0x0000FF);
		ret[3] = (byte) (a & 0x000000FF);

		return ret;
	}

	/**
	 * Padding Rule for ST-SYS
	 */
	public byte[] Padding(byte[] data, int len) {
		int i, block, rest;
		byte PadData[] = { (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00 };
		byte retData[];

		rest = len % 16;
		block = len / 16;

		if (rest != 0) {
			block++;
		}

		retData = new byte[block * 16];
		// modify by perious 2003.07.11
		// System.arraycopy (data, 0, retData, 0, data.length);
		System.arraycopy(data, 0, retData, 0, len);

		if (rest != 0) {
			for (i = 1; i <= SEED_BLOCK_SIZE; i++) {
				if (i <= data.length) {
					if (data[len - i] == 0x00) {
						continue;
					}
					if (data[len - i] == (byte) 0x80) {
						System.arraycopy(data, 0, retData, 0, data.length);
						try {
							System.arraycopy(PadData, 0, retData, len, SEED_BLOCK_SIZE);
							return retData;
						} catch (ArrayIndexOutOfBoundsException e) {
						}
					}
				}
				// return retData;
			}
			// return retData;
			System.arraycopy(PadData, 0, retData, len, SEED_BLOCK_SIZE - rest);
		}

		return retData;
	}

	/**
	 * UnPadding Rule for ST-SYS
	 */
	public int UnPadding(byte[] data, int len) {
		byte ch;
		int i;

		for (i = 1; i <= 16; i++) {
			// modify by perious 2003.07.11
			// ch = (byte) data[data.length -i];
			ch = data[len - i];
			if (ch == 0x00) {
				continue;
			} else if (ch == (byte) 0x80) {
				return len - i;
			} else {
				break;
			}
		}
		return len;
	}

	/**
	 * pbData를 pdwRoundKey로 암호화.
	 */
	public byte[] SeedEncrypt(byte[] pbPlainData, int[] pdwRoundKey) {
		// int i;
		byte[] pbData = new byte[pbPlainData.length];
		System.arraycopy(pbPlainData, 0, pbData, 0, pbPlainData.length);
		System.arraycopy(pdwRoundKey, 0, K, 0, pdwRoundKey.length);

		if (nSeedVersion == 9) {
			R0 = BytesToLong(pbData, 0);
			R1 = BytesToLong(pbData, 4);
			L0 = BytesToLong(pbData, 8);
			L1 = BytesToLong(pbData, 12);

			// BIG ENDIAN 처리 확인해볼것.

			SeedRound1L(L0, L1, R0, R1, 0);

			SeedRound1R(R0, R1, L0, L1, 2); // 2

			SeedRound1L(L0, L1, R0, R1, 4); // 3

			SeedRound1R(R0, R1, L0, L1, 6); // 4

			SeedRound1L(L0, L1, R0, R1, 8); // 5

			SeedRound1R(R0, R1, L0, L1, 10); // 6

			SeedRound1L(L0, L1, R0, R1, 12); // 7

			SeedRound1R(R0, R1, L0, L1, 14); // 8

			SeedRound1L(L0, L1, R0, R1, 16); // 9

			SeedRound1R(R0, R1, L0, L1, 18); // 10

			SeedRound1L(L0, L1, R0, R1, 20); // 11

			SeedRound1R(R0, R1, L0, L1, 22); // 12

			SeedRound1L(L0, L1, R0, R1, 24); // 13

			SeedRound1R(R0, R1, L0, L1, 26); // 14

			SeedRound1L(L0, L1, R0, R1, 28); // 15

			SeedRound1R(R0, R1, L0, L1, 30); // 16

			// BIG ENDIAN
			System.arraycopy(LongToBytes(L0), 0, pbData, 0, 4);
			System.arraycopy(LongToBytes(L1), 0, pbData, 4, 4);
			System.arraycopy(LongToBytes(R0), 0, pbData, 8, 4);
			System.arraycopy(LongToBytes(R1), 0, pbData, 12, 4);
		} else {
			L0 = BytesToLong(pbData, 0);
			L1 = BytesToLong(pbData, 4);
			R0 = BytesToLong(pbData, 8);
			R1 = BytesToLong(pbData, 12);

			SeedRoundL(L0, L1, R0, R1, 0); // 1
			SeedRoundR(R0, R1, L0, L1, 2); // 2
			SeedRoundL(L0, L1, R0, R1, 4); // 3
			SeedRoundR(R0, R1, L0, L1, 6); // 4
			SeedRoundL(L0, L1, R0, R1, 8); // 5
			SeedRoundR(R0, R1, L0, L1, 10); // 6
			SeedRoundL(L0, L1, R0, R1, 12); // 7
			SeedRoundR(R0, R1, L0, L1, 14); // 8
			SeedRoundL(L0, L1, R0, R1, 16); // 9
			SeedRoundR(R0, R1, L0, L1, 18); // 10
			SeedRoundL(L0, L1, R0, R1, 20); // 11
			SeedRoundR(R0, R1, L0, L1, 22); // 12

			if (NoRounds == 16) {
				SeedRoundL(L0, L1, R0, R1, 24); // 13
				SeedRoundR(R0, R1, L0, L1, 26); // 14
				SeedRoundL(L0, L1, R0, R1, 28); // 15
				SeedRoundR(R0, R1, L0, L1, 30); // 16
			}

			System.arraycopy(LongToBytes(R0), 0, pbData, 0, 4);
			System.arraycopy(LongToBytes(R1), 0, pbData, 4, 4);
			System.arraycopy(LongToBytes(L0), 0, pbData, 8, 4);
			System.arraycopy(LongToBytes(L1), 0, pbData, 12, 4);

		}

		return pbData;
	}

	/**
	 * pbData를 pdwRoundKey로 복호화.
	 */
	public byte[] SeedDecrypt(byte[] pbCipherData, int[] pdwRoundKey) {
		byte[] pbData = new byte[pbCipherData.length];
		System.arraycopy(pbCipherData, 0, pbData, 0, pbCipherData.length);
		System.arraycopy(pdwRoundKey, 0, K, 0, pdwRoundKey.length);

		if (nSeedVersion == 9) {
			R0 = BytesToLong(pbData, 0);
			R1 = BytesToLong(pbData, 4);
			L0 = BytesToLong(pbData, 8);
			L1 = BytesToLong(pbData, 12);

			SeedRound1L(L0, L1, R0, R1, 30);
			SeedRound1R(R0, R1, L0, L1, 28);
			SeedRound1L(L0, L1, R0, R1, 26);
			SeedRound1R(R0, R1, L0, L1, 24);
			SeedRound1L(L0, L1, R0, R1, 22);
			SeedRound1R(R0, R1, L0, L1, 20);
			SeedRound1L(L0, L1, R0, R1, 18);
			SeedRound1R(R0, R1, L0, L1, 16);
			SeedRound1L(L0, L1, R0, R1, 14);
			SeedRound1R(R0, R1, L0, L1, 12);
			SeedRound1L(L0, L1, R0, R1, 10);
			SeedRound1R(R0, R1, L0, L1, 8);
			SeedRound1L(L0, L1, R0, R1, 6);
			SeedRound1R(R0, R1, L0, L1, 4);
			SeedRound1L(L0, L1, R0, R1, 2);
			SeedRound1R(R0, R1, L0, L1, 0);

			System.arraycopy(LongToBytes(L0), 0, pbData, 0, 4);
			System.arraycopy(LongToBytes(L1), 0, pbData, 4, 4);
			System.arraycopy(LongToBytes(R0), 0, pbData, 8, 4);
			System.arraycopy(LongToBytes(R1), 0, pbData, 12, 4);
		} else {
			L0 = BytesToLong(pbData, 0);
			L1 = BytesToLong(pbData, 4);
			R0 = BytesToLong(pbData, 8);
			R1 = BytesToLong(pbData, 12);

			if (NoRounds == 16) {
				SeedRoundL(L0, L1, R0, R1, 30);
				SeedRoundR(R0, R1, L0, L1, 28);
				SeedRoundL(L0, L1, R0, R1, 26);
				SeedRoundR(R0, R1, L0, L1, 24);
			}

			SeedRoundL(L0, L1, R0, R1, 22);
			SeedRoundR(R0, R1, L0, L1, 20);
			SeedRoundL(L0, L1, R0, R1, 18);
			SeedRoundR(R0, R1, L0, L1, 16);
			SeedRoundL(L0, L1, R0, R1, 14);
			SeedRoundR(R0, R1, L0, L1, 12);
			SeedRoundL(L0, L1, R0, R1, 10);
			SeedRoundR(R0, R1, L0, L1, 8);
			SeedRoundL(L0, L1, R0, R1, 6);
			SeedRoundR(R0, R1, L0, L1, 4);
			SeedRoundL(L0, L1, R0, R1, 2);
			SeedRoundR(R0, R1, L0, L1, 0);

			System.arraycopy(LongToBytes(R0), 0, pbData, 0, 4);
			System.arraycopy(LongToBytes(R1), 0, pbData, 4, 4);
			System.arraycopy(LongToBytes(L0), 0, pbData, 8, 4);
			System.arraycopy(LongToBytes(L1), 0, pbData, 12, 4);
		}

		return pbData;
	}

	/**
	 * Ascii 정보를 Hex 값으로 리턴한다.
	 */
	public byte[] convertAsciiToHex(String strSource) {
		int i, j;
		char byTmp;

		byte[] bySource = strSource.getBytes();
		byte[] byDest = new byte[bySource.length / 2];

		for (i = 0, j = 0; i < byDest.length; i++, j++) {
			byTmp = (char) bySource[j];
			if ((byTmp - 0x30) < 10)
				byDest[i] = (byte) (byTmp - 0x30);
			else
				byDest[i] = (byte) (((byTmp - 0x41) + 10));
			byDest[i] <<= 4;
			byDest[i] &= 0xF0;
			j++;

			byTmp = (char) bySource[j];
			if ((byTmp - 0x30) < 10)
				byDest[i] |= (byte) (byTmp - 0x30);
			else
				byDest[i] |= (byte) (((byTmp - 0x41) + 10));
		}
		return byDest;
	}

	public byte[] mem_xor(byte[] ret_data, byte[] data, int len, int point) {
		int i;

		for (i = 0; i < len; i++) {
			ret_data[i] ^= data[i + point];
		}
		return ret_data;
	}
}
