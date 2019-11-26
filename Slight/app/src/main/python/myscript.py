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
        ['fun', 'amusement', 'interest', 'exciting', 'happy', 'pleasant', 'enjoyable', 'joyful', 'cheerful', 'happy', 'merry',
         'delightful', 'exhilarating', 'boon', 'simpatico', 'mirthful', 'roth', 'riant', 'joyous', 'rollicking','wondweful', 'kicking',
         'stirring', 'glad', 'stoked'
                             'desire', 'wish', 'aspiration', 'avid', 'relieved', 'unburdened', 'gratifying', 'satisfying', 'dramatic', 'moving', 'touching',
         'overwhelming', 'thrilled', 'wonder', 'awe', 'marvel', 'impressive', 'ecstasy', 'rapture', 'blissful', 'entrancing', 'charming',
         'nympholepsy', 'jolly', 'rosy', 'hopeful', 'bright', 'wishful', 'content', 'heartwarming', 'sufficient', 'ample', 'enough',
         'pride', 'proud', 'achievement', 'accomplishment', 'fulfillment', 'worthwhile', 'fruitful', 'rewarding', 'boast', 'honor', 'glory',
         'kudos', 'honour', 'priviledge', 'triumph', 'jubilance', 'ravishment', 'arouse', 'luck', 'fortune', 'relief', 'comfortable',
         'relaxed', 'easy', 'comfy', 'peaceful', 'calm', 'restful', 'quiet', 'informal', 'homey', 'love', 'cherish', 'beloved', 'lurve',
         'romance', 'caritas', 'thankful', 'grateful', 'obliged', 'glad', 'thankworthy', 'welcome', 'inspiring', 'flutter']

    SADNESS = \
        ['futility', 'vain', 'futlie', 'nihilism', 'fugacious', 'idle', 'dejected', 'dispirited', 'despondent', 'hollow',
         'resign', 'empty', 'boring', 'tiresome', 'wearisome', 'irksome', 'longwinded', 'dull', 'tedious', 'monotonous', 'lengthy', 'stodgy',
         'regret', 'repent', 'rue', 'remorse', 'lonely', 'solitary', 'lonesome', 'melancholy', 'forlorn', 'gloomy', 'desolated',
         'reclusive', 'moody', 'desert', 'alienation', 'isolated', 'depressed', 'loss', 'dejection', 'gessepany', 'sad', 'plaintive',
         'disconsolate', 'moody', 'grief', 'plead', 'sob', 'morn', 'doleful', 'unhappy', 'hurt', 'unfair', 'cruel', 'unfeeling', 'heartless',
         'harsh', 'coldly', 'bitter', 'upset', 'destressed', 'annoyed']

    ANGER = \
        ['fucking', 'dratted', 'offenseful', 'hateful', 'detestable', 'naughty', 'dislike', 'hostility', 'antaginism', 'animosity',
         'antipathy', 'disapproval', 'animus', 'enmity', 'anger', 'rage', 'fury', 'resentment', 'indignation', 'wrath', 'enrage', 'ire',
         'bile', 'mortifying', 'affronted', 'dodgasted', 'outrage', 'exasperation', 'displeased', 'miffy', 'pained', 'irritating',
         'raspingly', 'betray', 'treachery', 'vexation', 'grudge', 'reproachful', 'dissatisfaction', 'discontent', 'complaint',
         'oppression', 'dejection', 'abaissement', 'mortification', 'disappoint', 'envy', 'jealous', 'aggravated', 'peeve',
         'nasty', 'saucy', 'cheeky', 'pert', 'spiteful', 'impudent', 'mad']

    FEAR = \
        ['frightening', 'terrifying', 'horrifying', 'ghastly', 'gruesome', 'macabre', 'eldritch', 'unearthly', 'gooseflesh', 'hideous',
         'terrible', 'grisly', 'creepy', 'fearful', 'afraid', 'scared', 'dreaded', 'bogey', 'horrific', 'shock', 'stun', 'astonished',
         'astounded', 'startled', 'anxious', 'apprehension', 'unessiness', 'nervous', 'edgy', 'impatient', 'jittery', 'clutched', 'fretted']

    DISGUST = \
        ['abominate', 'loath', 'detastation', 'abhorrence', 'revulsion', 'aversion', 'repugnance', 'disrelish', 'contempt',
         'scorn', 'despise', 'contemn', 'disgusting', 'nauseating', 'yucky', 'sickening', 'repellent', 'repulsive', 'disillusion',
         'unpleasant', 'discomfort', 'uneasiness', 'unpleasure', 'disamenity', 'umbrage', 'queerness']

    RESTLESS = \
        ['pathetic', 'wailful', 'ardent', 'homesick', 'miss', 'yearn', 'longing', 'sympathy', 'pity', 'compassion', 'miserable',
         'lacerant', 'woeful', 'poor', 'wretch', 'commiserable', 'sorry', 'worry', 'concern', 'care', 'enbarrassed', 'disconcerted',
         'confuse', 'puzzled', 'perplexed', 'dilemma', 'baffled', 'absurd', 'ridiculous', 'nonsensial', 'preposterous', 'sublime',
         'ashamed', 'shameful', 'disgraceful', 'reprehensible', 'inglorious', 'bashful', 'shy', 'wusted', 'cringeworthy', 'unmentionable',
         'humiliated', 'ignominious', 'opprobrious', 'guilt', 'compunction', 'disturbed', 'complicated', 'intricacy', 'involution',
         'stuffy', 'stifling', 'suffocating', 'airless', 'poky', 'afflicting', 'dismal', 'direful', 'crushing', 'despair', 'hopelessness',
         'frustration', 'discourageed', 'reversal', 'backset', 'flameout', 'unhappiness', 'misfortune', 'unfortunate', 'unlucky', 'painful',
         'distressed', 'stern', 'urgent', 'desperate', 'stringent', 'clamant', 'impend']
    JoySum = 0
    SadnessSum = 0
    AngerSum = 0
    FearSum = 0
    DisgustSum = 0
    RestlessSum = 0

    ko_blob = textblob.TextBlob(input_)
    if ko_blob.detect_language() == 'en':
        input_text = ko_blob
    else:
        input_text = ko_blob.translate(to='en')
    input_text = str(input_text)

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
        if (k in JOY) == True:
            #print(k, round(sumVec[vector.vocabulary_[k]], 3))
            JoySum += sumVec[vector.vocabulary_[k]]

        if (k in SADNESS) == True:
            #print(k, round(sumVec[vector.vocabulary_[k]], 3))
            SadnessSum += sumVec[vector.vocabulary_[k]]

        if (k in ANGER) == True:
            #print(k, round(sumVec[vector.vocabulary_[k]], 3))
            AngerSum += sumVec[vector.vocabulary_[k]]
        if (k in FEAR) == True:
            #print(k, round(sumVec[vector.vocabulary_[k]], 3))
            FearSum += sumVec[vector.vocabulary_[k]]

        if (k in DISGUST) == True:
            #print(k, round(sumVec[vector.vocabulary_[k]], 3))
            DisgustSum += sumVec[vector.vocabulary_[k]]

        if (k in RESTLESS) == True:
            #print(k, round(sumVec[vector.vocabulary_[k]], 3))
            RestlessSum += sumVec[vector.vocabulary_[k]]

    resArr = [JoySum, SadnessSum, AngerSum, FearSum, DisgustSum, RestlessSum]

    res = resArr.index(max(resArr))
    res = str(res)
    return res
