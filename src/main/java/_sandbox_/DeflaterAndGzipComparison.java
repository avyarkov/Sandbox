package _sandbox_;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.*;

public class DeflaterAndGzipComparison {

    public static void main(String[] args) {
        String[] strings = new String[4];
        strings[0] = "Hello, World!";
        strings[1] = "aaaaaaaaaaaaaaaaaaaa";
        strings[2] = """
                There was a young lady of Niger
                Who smiled as she rode on a tiger;
                They returned from the ride
                With the lady inside,
                And the smile on the face of the tiger.""";
        strings[3] = SHORT_NOVEL;
        for (String string : strings) {
            byte[] inputBytes = string.getBytes(StandardCharsets.UTF_8);

            byte[] deflaterOutputBytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (DeflaterOutputStream dos = new DeflaterOutputStream(baos)) {
                    dos.write(inputBytes);
                }
                deflaterOutputBytes = baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] gzipOuputBytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (GZIPOutputStream gzipos = new GZIPOutputStream(baos)) {
                    gzipos.write(inputBytes);
                }
                gzipOuputBytes = baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("input: " + inputBytes.length +
                    ", deflater: " + deflaterOutputBytes.length +
                    ", gzip: " + gzipOuputBytes.length);

            byte[] inflatedBytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (InflaterOutputStream ios = new InflaterOutputStream(baos)) {
                    ios.write(deflaterOutputBytes);
                }
                inflatedBytes = baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String inflatedString = new String(inflatedBytes, StandardCharsets.UTF_8);
            System.out.println(string.equals(inflatedString));

            byte[] gzipDecompressedBytes;
            try (ByteArrayInputStream bais = new ByteArrayInputStream(gzipOuputBytes)) {
                try (GZIPInputStream gzipis = new GZIPInputStream(bais)) {
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        int buf;
                        while ((buf = gzipis.read()) != -1) {
                            baos.write(buf);
                        }
                        gzipDecompressedBytes = baos.toByteArray();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String gzipDecompressedString = new String(gzipDecompressedBytes);
            System.out.print(string.equals(gzipDecompressedString) + " ");

            byte[] gzipDecompressedBytes2;
            try (ByteArrayInputStream bais = new ByteArrayInputStream(gzipOuputBytes)) {
                try (GZIPInputStream gzipis = new GZIPInputStream(bais)) {
                    gzipDecompressedBytes2 = gzipis.readAllBytes();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String gzipDecompressedString2 = new String(gzipDecompressedBytes2);
            System.out.println(string.equals(gzipDecompressedString2));
        }

    }

    public static String SHORT_NOVEL = """
            The Roads We Take
            by O. Henry
                        
            TWENTY miles West of Tucson, the "Sunset Express" stopped at a tank to take on water. Besides the aqueous, addition the engine of that famous flyer acquired some other things that were not good for it.
                        
            While the fireman was lowering the feeding hose, Bob Tidball, "Shark" Dodson and a quarter-bred Creek Indian called John Big Dog climbed on the engine and showed the engineer three round orifices in pieces of ordnance that the carried. These orifices so impressed the engineer with their possibilities that he raised both hands in a gesture such as accompanies the ejaculation "Do tell!"
                        
            At the crisp command of Shark Dodson, who was leader of the attacking force the engineer descended to the ground and uncoupled the engine and tender. Then John Big Dog, perched upon the coal, sportively held two guns upon the engine driver and the fireman, and suggested that they run the engine fifty yards away and there await further orders.
                        
            Shark Dodson and Bob Tidball, scorning to put such low-grade ore as the passengers through the mill, struck out for the rich pocket of the express car. They found the messenger serene in the belief that the "Sunset Express" was taking on nothing more stimulating and dangerous than aqua pura. While Bob was knocking this idea out of his head with the butt-end of his six-shooter Shark Dodson was already dosing the express-car safe with dynamite.
                        
            The safe exploded to the tune of $30,000, all gold and currency. The passengers thrust their heads casually out of the windows to look for the thunder-cloud. The conductor jerked at the bell-rope, which sagged down loose and unresisting, at his tug. Shark Dodson and Bob Tidball, with their booty in a stout canvas bag, tumbled out of the express car and ran awkwardly in their high-heeled boots to the engine.
                        
            The engineer, sullenly angry but wise, ran the engine, according to orders, rapidly away from the inert train. But before this was accomplished the express messenger, recovered from Bob Tidball's persuader to neutrality, jumped out of his car with a Winchester rifle and took a trick in the game. Mr. John Big Dog, sitting on the coal tender, unwittingly made a wrong lead by giving an imitation of a target, and the messenger trumped him. With a ball exactly between his shoulder blades the Creek chevalier of industry rolled off to the ground, thus increasing the share of his comrades in the loot by one-sixth each.
                        
            Two miles from the tank the engineer was ordered to stop.
                        
            The robbers waved a defiant adieu and plunged down the steep slope into the thick woods that lined the track. Five minutes of crashing through a thicket of chapparal brought them to open woods, where three horses were tied to low-hanging branches. One was waiting for John Big Dog, who would never ride by night or day again. This animal the robbers divested of saddle and bridle and set free. They mounted the other two with the bag across one pommel, and rode fast and with discretion through the forest and up a primeval, lonely gorge. Here the animal that bore Bob Tidball slipped on a mossy boulder and broke a foreleg. They shot him through the head at once and sat down to hold a council of flight. Made secure for the present by the tortuous trail they- had travelled, the question of time was no longer so big. Many miles and hours lay between them and the spryest posse that could follow. Shark Dodson's horse, with trailing rope and dropped bridle, panted and cropped thankfully of the grass along the stream in the gorge. Bob Tidball opened the sack, drew out double handfuls of the neat packages of currency and the one sack of gold and chuckled with the glee of a child.
                        
            "Say, you old double-decked pirate," he called joyfully to Dodson, "you said we could do it -- you got a head for financing that knocks the horns off of anything in Arizona."
                        
            "What are we going to do about a hoss for you, Bob? We ain't got long to wait here. They'll be on our trail before daylight in the mornin'."
                        
            "Oh, I guess that cayuse of yourn'll carry double for a while," answered the sanguine Bob. "We'll annex the first animal we come across. By jingoes, we made a haul, didn't we? Accordin' to the marks on this money there's $30,000 -- $15,000 apiece!"
                        
            "It's short of what I expected," said Shark Dodson, kicking softly at the packages with the toe of his boot and then he looked pensively at the wet sides of his tired horse.
                        
            "Old Bolivar's mighty nigh played out," he said, slowly. "I wish that sorrel of yours hadn't got hurt."
                        
            "So do I," said Bob, heartily, "but it can't be helped. Bolivar's got plenty of bottom -- he'll get us both far enough to get fresh mounts. Dang it, Shark, I can't belp thinkin' how funny it is that an Easterner like you can come out here and give us Western fellows cards and spades in the desperado business. What part of the East was you from, anyway?"
                        
            "New York State," said Shark Dodson, sitting down on a boulder and chewing a twig. "I was born on a farm in Ulster County. I ran away from home when I was seventeen. It was an accident my coming West. I was walkin' along the road with my clothes in a bundle, makin' for New York City. I had an idea of goin' there and makin' lots of money. I always felt like I could do it. I came to a place one evenin' where the road forked and I didn't know which fork to take. I studied about it for half an hour, and then I took the left- hand. That night I run into the camp of a Wild West show that was travellin' among the little towns, and I went West with it. I've often wondered if I wouldn't have turned out different if I'd took the other road."
                        
            "Oh, I reckon you'd have ended up about the same," said Bob Tidball, cheerfully philosophical. "It ain't the roads we take; it's what's inside of us that makes us turn out the way we do."
                        
            Shark Dodson got up and leaned against a tree.
                        
            "I'd a good deal rather that sorrel of yourn hadn't hurt himself, Bob," he said again, almost pathetically.
                        
            "Same here," agreed Bob; "he was sure a first-rate kind of a crowbait. But Bolivar, he'll pull us through all right. Reckon we'd better be movin' on, hadn't we, Shark? I'll bag this boodle ag'in and we'll hit the trail for higher timber."
                        
            Bob Tidball replaced the spoil in the bag and tied the mouth of it tightly with a cord. When he looked up the most prominent object that he saw was the muzzle of Shark Dodson's .45 held upon him without a waver.
                        
            "Stop your funnin'," said Bob, with a grin. "We got to be hittin' the breeze."
                        
            "Set still," said Shark. "You ain't goin' to hit no breeze, Bob. I hate to tell you, but there ain't any chance for but one of us. Bolivar, he's plenty tired, and he can't carry double."
                        
            "We been pards, me and you, Shark Dodson, for three year," Bob said quietly. "We've risked our lives together time and again. I've always give you a square deal, and I thought you was a man. I've heard some queer stories about you shootin' one or two men in a peculiar way, but I never believed 'em. Now if you're just havin' a little fun with me, Shark, put your gun up, and we'll get on Bolivar and vamose. If you mean to shoot -- shoot, you blackhearted son of a tarantula!"
                        
            Shark Dodson's face bore a deeply sorrowful look. "You don't know how bad I feel," he sighed, "about that sorrel of yourn breakin' his leg, Bob."
                        
            The expression on Dodson's face changed in an instant to one of cold ferocity mingled with inexorable cupidity. The soul of the man showed itself for a moment like an evil face in the window of a reputable house.
                        
            Truly Bob Tidball was never to "hit the breeze" again. The deadly .45 of the false friend cracked and filled the gorge with a roar that the walls hurled back with indignant echoes. And Bolivar, unconscious accomplice, swiftly bore away the last of the holders-up of the "Sunset Express," not put to the stress of "carrying double."
                        
            But as "Shark" Dodson galloped away the woods seemed to fade from his view; the revolver in his right hand turned to the curved arm of a mahogany chair; his saddle was strangely upholstered, and he opened his eyes and saw his feet, not in stirrups, but resting quietly on the edge of a quartered-oak desk.
                        
            I am telling you that Dodson, of the firm of Dodson & Decker, Wall Street brokers, opened his eyes. Peabody, the confidential clerk, was standing by his chair, hesitating to speak. There was a confused hum of wheels below, and the sedative buzz of an electric fan.
                        
            "Ahem! Peabody," said Dodson, blinking. "I must have fallen asleep. I had a most remarkable dream. What is it, Peabody?"
                        
            "Mr. Williams, sir, of Tracy & Williams, is outside. He has come to settle his deal in X. Y. Z. The market caught him short, sir, if you remember."
                        
            "Yes, I remember. What is X. Y. Z. quoted at to-day, Peabody?"
                        
            "One eighty-five, sir."
                        
            "Then that's his price."
                        
            "Excuse me," said Peabody, rather nervously "for speaking of it, but I've been talking to Williams. He's an old friend of yours, Mr. Dodson, and you practically have a corner in X. Y. Z. I thought you might -- that is, I thought you might not remember that he sold you the stock at 98. If he settles at the market price it will take every cent he has in the world and his home too to deliver the shares."
                        
            The expression on Dodson's face changed in an instant to one of cold ferocity mingled with inexorable cupidity. The soul of the man showed itself for a moment like an evil face in the window of a reputable house.
                        
            "He will settle at one eighty-five," said Dodson. "Bolivar cannot carry double."
            """;
}
