# def test(a):
#     return a

import numpy as np
import textblob
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer

import nltk


def test(input_):

    nltk.data.path.append("/data/data/cau.injiyong.slight/files/nltk_data")

    B_INCR = 0.293
    B_DECR = -0.293

    NEGATE = \
        ["aint", "arent", "cannot", "cant", "couldnt", "darent", "didnt", "doesnt",
         "ain't", "aren't", "can't", "couldn't", "daren't", "didn't", "doesn't",
         "dont", "hadnt", "hasnt", "havent", "isnt", "mightnt", "mustnt", "neither",
         "don't", "hadn't", "hasn't", "haven't", "isn't", "mightn't", "mustn't",
         "neednt", "needn't", "never", "none", "nope", "nor", "not", "nothing", "nowhere",
         "oughtnt", "shant", "shouldnt", "uhuh", "wasnt", "werent",
         "oughtn't", "shan't", "shouldn't", "uh-uh", "wasn't", "weren't",
         "without", "wont", "wouldnt", "won't", "wouldn't", "rarely", "seldom", "despite", "n't", "no"]

    BOOSTER_DICT = \
        {"absolutely": B_INCR, "amazingly": B_INCR, "awfully": B_INCR,
         "completely": B_INCR, "considerable": B_INCR, "considerably": B_INCR,
         "decidedly": B_INCR, "deeply": B_INCR, "effing": B_INCR, "enormous": B_INCR, "enormously": B_INCR,
         "entirely": B_INCR, "especially": B_INCR, "exceptional": B_INCR, "exceptionally": B_INCR,
         "extreme": B_INCR, "extremely": B_INCR,
         "fabulously": B_INCR, "flipping": B_INCR, "flippin": B_INCR, "frackin": B_INCR, "fracking": B_INCR,
         "fricking": B_INCR, "frickin": B_INCR, "frigging": B_INCR, "friggin": B_INCR, "fully": B_INCR,
         "fuckin": B_INCR, "fucking": B_INCR, "fuggin": B_INCR, "fugging": B_INCR,
         "greatly": B_INCR, "hella": B_INCR, "highly": B_INCR, "hugely": B_INCR,
         "incredible": B_INCR, "incredibly": B_INCR, "intensely": B_INCR,
         "major": B_INCR, "majorly": B_INCR, "more": B_INCR, "most": B_INCR, "particularly": B_INCR,
         "purely": B_INCR, "quite": B_INCR, "really": B_INCR, "remarkably": B_INCR,
         "so": B_INCR, "substantially": B_INCR,
         "thoroughly": B_INCR, "total": B_INCR, "totally": B_INCR, "tremendous": B_INCR, "tremendously": B_INCR,
         "uber": B_INCR, "unbelievably": B_INCR, "unusually": B_INCR, "utter": B_INCR, "utterly": B_INCR,
         "very": B_INCR,
         "almost": B_DECR, "barely": B_DECR, "hardly": B_DECR, "just enough": B_DECR,
         "kind of": B_DECR, "kinda": B_DECR, "kindof": B_DECR, "kind-of": B_DECR,
         "less": B_DECR, "little": B_DECR, "marginal": B_DECR, "marginally": B_DECR,
         "occasional": B_DECR, "occasionally": B_DECR, "partly": B_DECR,
         "scarce": B_DECR, "scarcely": B_DECR, "slight": B_DECR, "slightly": B_DECR, "somewhat": B_DECR,
         "sort of": B_DECR, "sorta": B_DECR, "sortof": B_DECR, "sort-of": B_DECR}

    JOY = \
        ['cute', 'smile', 'good', 'nice', 'cheerful', 'commanding', 'loving', 'hilarious', 'flutter', 'fun','funny', 'amusement', 'interest','interesting','interested', 'excite', 'exciting','excited', 'pleasant','pleasantly',
         'pleasure', 'enjoyable','enjoy', 'joyful','joy', 'cheer','cheery','cheerful', 'happy', 'happily','merry', 'delightful','delight',
         'exhilarating','exhilarate', 'boon','exhilarated', 'simpatico', 'mirthful',  'riant','joy', 'joyous', 'rollicking','rollick','wonderful',
         'glad','gladly', 'desire','desired', 'desirable','wish','wishes','aspire', 'aspiration', 'avid', 'relieve','relieving','relieved',
         'unburdened', 'unburden' ,'gratifying','gratify', 'gratified', 'satisfy','satisfaction','satisfied','satisfying', 'dramatic', 'moving',
         'touching', 'overwhelming','overwhelmed','overwhelm' ,'wonder', 'awe', 'marvel','marvelous', 'impressive',
         'impressed','impressing','impress' ,'ecstasy','ecstatic', 'rapture','raptured', 'blissful','bliss', 'entrancing','entranced', 'charming','charm',
         'nympholepsy', 'jolly', 'rosy', 'hopeful','hope','hopes','hopefully', 'bright','brightly', 'wishful', 'content', 'heartwarming', 'sufficient',
         'ample', 'enough', 'pride', 'proud','achieve', 'achievement','accomplish', 'accomplishment', 'fulfillment','fulfill', 'worthwhile', 'fruitful',
         'rewarding', 'boast','boastful', 'honor','honored' ,'glory','glorious', 'kudos', 'honour','honoured', 'priviledge', 'triumph', 'jubilance',
         'arouse','aroused','lucky', 'luck', 'fortune','fortunate', 'relief', 'comfortable','comfort','relax','relaxing', 'relaxed', 'easy', 'comfy',
         'peaceful','peace', 'calm', 'restful','rest', 'quiet', 'informal', 'homey', 'love','loved', 'cherish', 'beloved', 'lurve', 'romance', 'caritas',
         'thankful','thanks','thank', 'grateful', 'obliged',  'thankworthy', 'welcome','welcoming','welcomes', 'inspiring', 'inspire', 'inspired','flutter']

    SADNESS = \
        ['alone', 'coldhearted', 'unkind','bereft', 'bereavement', 'apathetic', 'apathy', 'crying', 'cry', 'cries', 'cried', 'disastrous', 'moldy', 'futility', 'vain','vanity', 'futlie', 'nihilism', 'fugacious', 'idle', 'dejected','deject','dejects','dejecting', 'dispirited','dispirit',
         'dispiriting','dispirits' 'despondent','despond','desponding','despondency','hollowed', 'hollow',
         'resign','resignation','resigning','resigned', 'empty', 'boredom', 'bored' ,'boring', 'tiresome', 'wearisome', 'irksome', 'longwinded', 'dull', 'tedious',
         'monotonous', 'lengthy', 'stodgy',
         'regret','regrets', 'repent','repents','repenting', 'rue','rues','rueing', 'remorse', 'lonely', 'solitary','solitarily', 'lonesome', 'melancholy',
         'forlorn', 'gloomy', 'desolate','desolating','desolated',
         'reclusive','reclusively', 'moody', 'desert','deserted', 'alienation','alienate','alienated','alienating', 'isolate', 'isolating', 'isolated',
         'depressed','depress','depressing','losses', 'loss','deject','dejected', 'dejection', 'gessepany', 'sad', 'sadly','sadness','plaintive',
         'disconsolate', 'grief','grieve','grieving','grieved', 'plead','plea', 'sob', 'morn', 'doleful', 'unhappy', 'hurts','hurt', 'unfair','unfairness',
         'cruelty', 'cruel', 'unfeeling', 'heartless', 'harsh', 'coldly', 'bitter','bitterness', 'upset']

    ANGER = \
        ['frown', 'frowning','annoy','annoying', 'annoyed', 'calamity', 'angry','anger', 'fucking', 'dratted','drat', 'offenseful','offense','hate', 'hateful', 'detest', 'detestable', 'naughty', 'dislike', 'hostility',
         'antaginism', 'animosity','hatred',
         'antipathy', 'disapproval','disapprove', 'animus', 'enmity', 'rage', 'fury', 'resentment','resent', 'indignation', 'wrath', 'enrage', 'ire',
         'bile', 'mortify', 'mortified', 'mortifying', 'affront', 'affronting', 'affronted', 'dodgasted', 'outraged', 'outrage', 'exasperation','exasperate',
         'exasperating', 'exasperated', 'displease','displeasing', 'displeased', 'miffy', 'pained', 'irritate','irritates', 'irritating',
         'raspingly', 'betray', 'treachery', 'vex', 'vexation', 'grudge','reproach', 'reproachful', 'dissatisfaction', 'discontent', 'complaint',
         'oppression','oppress', 'oppressed', 'abaissement', 'mortification', 'disappoint','disappointed','disappointment','disappointing', 'envy', 'jealous',
         'aggravate','aggravating', 'aggravated', 'peeve','peeved',
         'nasty', 'saucy', 'cheeky', 'pert', 'spiteful', 'impudent', 'mad']

    FEAR = \
        ['strange', 'retaliation', 'threaten','threatening','threat', 'menacing', 'menace', 'thrilled','thrill','thrilling', 'bloodcurdling','eerie','uncanny', 'agony', 'agonize', 'breakup', 'frightening','frightened','frighten','fright', 'terrifying','terrify','terrified', 'horrify','horror', 'horrifying', 'ghastly', 'gruesome', 'macabre',
         'eldritch', 'unearthly', 'gooseflesh', 'hideous', 'terrible', 'grisly', 'creepy','fear', 'fearful','feared', 'afraid','scare', 'scared',
         'dreaded','dread', 'bogey', 'horrific', 'shock','shocked', 'stun', 'astonished','astonish','astonished','astonishing', 'impatience', 'astounded',
         'astound','astounding', 'startled','startle', 'anxious','anxiousness', 'apprehension', 'uneasiness','uneasy', 'nervous', 'edgy', 'impatient',
         'jittery', 'clutched', 'fretted', 'scary','painful']

    DISGUST = \
        ['stinc','stinky', 'stinken', 'vomit', 'venomous', 'nausea','abominate', 'abomination', 'loath', 'abhorrence','abhorre', 'revulsion', 'aversion', 'repugnance', 'disrelish', 'contempt',
         'scorn', 'despise', 'contemn', 'disgusting','disgust', 'nauseating', 'yucky', 'sickening', 'repellent', 'repulsive', 'disillusion','disillusionment',
         'unpleasant', 'discomfort', 'unpleasure', 'disamenity', 'umbrage', 'queerness']

    RESTLESS = \
        ['ill', 'ache', 'diseased', 'disease', 'sick', 'weary', 'depleted', 'tired','sleepy', 'helpless', 'restless', 'pathetic','pathetically', 'wailful', 'ardent', 'homesick', 'miss','misses','missed', 'yearn','yearning','yearns', 'longing', 'sympathy','sympathetic',
         'sympathize', 'pity','pitiable', 'compassion','compassionate', 'miserable',
         'lacerant', 'woeful', 'poor', 'wretch', 'commiserable', 'sorry', 'worry','worries','worried', 'concern', 'care','cares', 'enbarrassed','enbarrass',
         'enbarrassing', 'disconcerted','disconcert','disconcerting',
         'confuse','confused','confusing','confusement', 'puzzling', 'puzzled', 'perplex' , 'perplexing', 'perplexed', 'dilemma', 'baffled', 'absurd',
         'ridiculous', 'nonsensial', 'preposterous', 'sublime',
         'shame', 'ashamed', 'shameful','shame', 'disgrace', 'disgraceful', 'reprehensible', 'inglorious', 'bashful', 'shy', 'wusted', 'cringeworthy', 'unmentionable',
         'humiliated','humiliate','humiliation', 'humiliating', 'ignominious', 'opprobrious', 'guilt','guilty', 'compunction', 'disturbed','disturbing','disturbes',
         'complicated', 'intricacy', 'involution', 'stuffy', 'stifling', 'stifle', 'stifles', 'suffocates', 'suffocate', 'suffocated', 'suffocating','disheartened',
         'airless', 'poky', 'afflicting','affliction','afflict', 'afflicts', 'dismal', 'direful', 'crushing', 'despair', 'hopelessness','hopeless','frustrate',
         'frustration','frustrating','frustrated', 'discourageed', 'reversal', 'backset', 'flameout', 'unhappiness', 'misfortune', 'unfortunate', 'unlucky',
         'distressed', 'stern', 'urgent', 'desperate', 'stringent','stringently', 'clamant','clamantly', 'impend','impending', 'impendly']
    try:
        JoySum = 0
        SadnessSum = 0
        AngerSum = 0
        FearSum = 0
        DisgustSum = 0
        RestlessSum = 0

        ko_blob = textblob.TextBlob(input_ + " (s)")

        if ko_blob.detect_language() == 'en':
            input_text = ko_blob
        else:
            input_text = ko_blob.translate(from_lang='ko', to='en')
        input_text = str(input_text.lower())

        input_strings = nltk.tokenize.sent_tokenize(input_text)

        vector = CountVectorizer()
        countVec = vector.fit_transform(input_strings).toarray()

        input_words = []
        for i in range(len(countVec)):
            tokenizer=nltk.tokenize.TreebankWordTokenizer()
            input_words.append(tokenizer.tokenize(input_strings[i]))

        countVec = np.asfarray(countVec)

        neg_words = []
        neg_words.extend(NEGATE)

        for i in range(len(countVec)):
            for k, word in enumerate(input_words[i]):
                if (word in neg_words) and (word in vector.vocabulary_):

                    if (countVec[i, vector.vocabulary_[word]] > 0):
                        if (k + 2 < len(input_words[i])):
                            word1 = input_words[i][k + 1]
                            word2 = input_words[i][k + 2]
                            if word1 in vector.vocabulary_:
                                countVec[i, vector.vocabulary_[word1]] -= 0.59
                            if word2 in vector.vocabulary_:
                                if word1 in neg_words:
                                    pass
                                else:
                                    countVec[i, vector.vocabulary_[word2]] -= 0.59

                        elif (k + 1 < len(input_words[i])):
                            word1 = input_words[i][k + 1]
                            if word1 in vector.vocabulary_:
                                countVec[i, vector.vocabulary_[word1]] -= 0.59
                        else:
                            pass

                scalar = 0.0

                if word in BOOSTER_DICT:
                    scalar = BOOSTER_DICT[word]
                if scalar != 0 and (word in vector.vocabulary_):
                    if (k + 1 < len(input_words[i])):
                        word1 = input_words[i][k + 1]
                        if word1 in vector.vocabulary_:
                            countVec[i, vector.vocabulary_[word1]] += scalar

        countVec = np.maximum(0, countVec)

        delta = 10**(-9)
        resVec = np.maximum(0, 1 + np.log(countVec + delta))

        lambda_ = 0.05
        tfidfv = TfidfVectorizer().fit(input_strings)
        resVec = resVec + lambda_ * (1 - tfidfv.transform(input_strings).toarray())

        sumVec = np.sum(resVec, axis=0)

        for k in vector.vocabulary_:
            rk = textblob.Word(k).lemmatize('v')
            if (rk in JOY) == True:
                JoySum += sumVec[vector.vocabulary_[k]]

            if (rk in SADNESS) == True:
                SadnessSum += sumVec[vector.vocabulary_[k]]

            if (rk in ANGER) == True:
                AngerSum += sumVec[vector.vocabulary_[k]]

            if (rk in FEAR) == True:
                FearSum += sumVec[vector.vocabulary_[k]]

            if (rk in DISGUST) == True:
                DisgustSum += sumVec[vector.vocabulary_[k]]

            if (rk in RESTLESS) == True:
                RestlessSum += sumVec[vector.vocabulary_[k]]

        resArr = [JoySum, SadnessSum, AngerSum, FearSum, DisgustSum, RestlessSum]

        res = resArr.index(max(resArr))
        res = str(res)

        return res

    except Exception as ex:
        return "6"